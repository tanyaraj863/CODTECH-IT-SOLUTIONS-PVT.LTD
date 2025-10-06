package com.example.recommender;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecommenderApp {

    private static final Logger LOGGER = Logger.getLogger(RecommenderApp.class.getName());

    public static void main(String[] args) {
        try {
            // Load CSV safely
            URL resource = RecommenderApp.class.getResource("/data/data.csv");
            if (resource == null) {
                throw new RuntimeException("data.csv not found in resources/data/");
            }

            File file;
            try {
                file = new File(resource.toURI());
            } catch (Exception e) {
                throw new RuntimeException("Error converting resource to File", e);
            }

            LOGGER.info("Loading file: " + file.getAbsolutePath());

            DataModel model = new FileDataModel(file);

            // ===== User-based recommender =====
            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, userSimilarity, model);
            GenericUserBasedRecommender userBasedRecommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);

            LOGGER.info("User-based recommendations for user 1:");
            List<RecommendedItem> userRecommendations = userBasedRecommender.recommend(1, 3);
            if (userRecommendations.isEmpty()) {
                LOGGER.info("No user-based recommendations found for user 1");
            } else {
                for (RecommendedItem recommendation : userRecommendations) {
                    LOGGER.info("  item: " + recommendation.getItemID() + ", score: " + recommendation.getValue());
                }
            }

            // ===== Item-based recommender =====
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
            GenericItemBasedRecommender itemBasedRecommender = new GenericItemBasedRecommender(model, itemSimilarity);

            LOGGER.info("Item-based recommendations for user 1:");
            List<RecommendedItem> itemRecommendations = itemBasedRecommender.recommend(1, 3);
            if (itemRecommendations.isEmpty()) {
                LOGGER.info("No item-based recommendations found for user 1");
            } else {
                for (RecommendedItem recommendation : itemRecommendations) {
                    LOGGER.info("  item: " + recommendation.getItemID() + ", score: " + recommendation.getValue());
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating recommendations", e);
        }
    }
}
