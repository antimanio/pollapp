package dat250.projects.pollapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;
import java.util.function.Supplier;


/**
 * @author Anton Tran
 * Utility class for caching data in Redis using JSON serialization.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Fetching data from Redis cache if available</li>
 *     <li>Fetching from a data source (e.g., DB) on cache miss</li>
 *     <li>Updating Redis cache after fetching fresh data</li>
 *     <li>Invalidating cache manually using a dedicated method</li>
 * </ul>
 * <p>
 * The ObjectMapper is configured with {@link JavaTimeModule} to support
 * Java 8 date/time classes (e.g., Instant, LocalDateTime).
 */

@Service
public class RedisService {
    private final JedisPooled jedis;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final String REDIS_HOST = "redis"; //change to redis later
    private static final int REDIS_PORT = 6379;

    public RedisService() {
        this.jedis = new JedisPooled(REDIS_HOST, REDIS_PORT);
    }

    /**
     * Fetches data from Redis cache if available; otherwise fetches from a supplier (e.g., database),
     * updates the cache, and returns the fresh data.
     *
     * @param key           Redis key name
     * @param fetchFromDb   Function to fetch the data from DB or another source if cache is missing
     * @param expireSeconds Cache expiration time in seconds
     * @param typeRef       Type reference for JSON deserialization
     * @param <T>           Type of the object being cached
     * @return The cached or freshly fetched data
     */
    public <T> T fetchOrUpdateCache(String key, Supplier<T> fetchFromDb, int expireSeconds, TypeReference<T> typeRef) {
        try {
            // Try fetch from Redis
            String cached = jedis.get(key);

            if (cached != null) {
                System.out.println("[RedisCache] Cache hit for key: " + key);
                return mapper.readValue(cached, typeRef); // Deserialization
            }

            System.out.println("[RedisCache] Cache miss for key: " + key + ", fetching from DB...");

            // Fetch from DB
            T data = fetchFromDb.get();
            if (data != null) {
                // Invalidate old key first
                invalidateCache(key);

                // Store new value in json string (serialization)
                String json = mapper.writeValueAsString(data);
                jedis.setex(key, expireSeconds, json);
                System.out.println("[RedisCache] Cache updated for key: " + key);
            }

            return data;
        } catch (Exception e) {
            throw new RuntimeException("Redis cache error for key '" + key + "'", e);
        }
    }

    public void invalidateCache(String key) {
        try {
            jedis.del(key);
            System.out.println("[RedisCache] Cache invalidated for key: " + key);
        } catch (Exception e) {
            throw new RuntimeException("Redis cache invalidation error for key '" + key + "'", e);
        }
    }

}
