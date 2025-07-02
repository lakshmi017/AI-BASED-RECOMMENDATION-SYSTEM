import java.util.*;
import java.lang.Math;

public class RecommendationSystem {

    // Inner Product class
    static class Product {
        private String name;
        private List<String> features;

        public Product(String name, List<String> features) {
            this.name = name;
            this.features = features;
        }

        public String getName() {
            return this.name;
        }

        public List<String> getFeatures() {
            return this.features;
        }
    }

    // AI Recommendation Engine
    static class RecommendationEngine {

        public static double calculateSimilarity(Product p1, Product p2) {
            Set<String> allFeatures = new HashSet<>(p1.getFeatures());
            allFeatures.addAll(p2.getFeatures());

            int[] vector1 = new int[allFeatures.size()];
            int[] vector2 = new int[allFeatures.size()];

            int i = 0;
            for (String feature : allFeatures) {
                vector1[i] = p1.getFeatures().contains(feature) ? 1 : 0;
                vector2[i] = p2.getFeatures().contains(feature) ? 1 : 0;
                i++;
            }

            return cosineSimilarity(vector1, vector2);
        }

        private static double cosineSimilarity(int[] vec1, int[] vec2) {
            int dot = 0, normA = 0, normB = 0;
            for (int i = 0; i < vec1.length; i++) {
                dot += vec1[i] * vec2[i];
                normA += vec1[i] * vec1[i];
                normB += vec2[i] * vec2[i];
            }
            return dot / (Math.sqrt(normA) * Math.sqrt(normB));
        }

        public static List<Product> recommend(Product userLiked, List<Product> products, int topN) {
            Map<Product, Double> scores = new HashMap<>();
            for (Product product : products) {
                if (!product.getName().equals(userLiked.getName())) {
                    double similarity = calculateSimilarity(userLiked, product);
                    scores.put(product, similarity);
                }
            }

            return scores.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .limit(topN)
                    .map(Map.Entry::getKey)
                    .toList();
        }
    }

    // Main method
    public static void main(String[] args) {
        List<Product> catalog = List.of(
            new Product("Laptop", List.of("electronics", "computer", "portable")),
            new Product("Desktop", List.of("electronics", "computer", "gaming")),
            new Product("Smartphone", List.of("electronics", "mobile", "touchscreen")),
            new Product("Tablet", List.of("electronics", "portable", "touchscreen")),
            new Product("Camera", List.of("electronics", "photo", "portable"))
        );

        Product userLiked = new Product("Smartphone", List.of("electronics", "mobile", "touchscreen"));

        List<Product> recommendations = RecommendationEngine.recommend(userLiked, catalog, 3);

        System.out.println("Top Recommendations:");
        for (Product product : recommendations) {
            System.out.println("- " + product.getName());
        }
    }
}
