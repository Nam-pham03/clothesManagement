package com.example.project_prm.Repository;

import android.content.Context;
import com.example.project_prm.Dao.RatingDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Rating;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RatingRepository {
    private final RatingDao ratingDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RatingRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        ratingDao = db.ratingDao();
    }

    public void insert(Rating rating) {
        executorService.execute(() -> ratingDao.insert(rating));
    }

    public List<Rating> getRatingsByProduct(int productId) {
        return ratingDao.getRatingsByProduct(productId);
    }
}
