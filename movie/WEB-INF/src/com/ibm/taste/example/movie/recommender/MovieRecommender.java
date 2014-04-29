package com.ibm.taste.example.movie.recommender;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;

/**
 * A simple {@link Recommender} implemented for the GroupLens demo.
 */
public final class MovieRecommender implements Recommender {

  private final Recommender recommender;

  /**
   * @throws IOException if an error occurs while creating the {@link GroupLensDataModel}
   * @throws TasteException if an error occurs while initializing this {@link GroupLensRecommender}
   */
  public MovieRecommender() throws IOException, TasteException {
	  this(new MovieDataModel());
  }

  /**
   * <p>Alternate constructor that takes a {@link DataModel} argument, which allows this {@link Recommender}
   * to be used with the {@link org.apache.mahout.cf.taste.eval.RecommenderEvaluator} framework.</p>
   *
   * @param dataModel data model
   * @throws TasteException if an error occurs while initializing this {@link GroupLensRecommender}
   */
  public MovieRecommender(DataModel dataModel) throws TasteException {
	  
    recommender = new CachingRecommender(new SlopeOneRecommender(dataModel));
  }

  
  public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
    return recommender.recommend(userID, howMany);
  }

 
  public List<RecommendedItem> recommend(long userID, int howMany, Rescorer<Long> rescorer)
          throws TasteException {
    return recommender.recommend(userID, howMany, rescorer);
  }

  
  public float estimatePreference(long userID, long itemID) throws TasteException {
    return recommender.estimatePreference(userID, itemID);
  }

  
  public void setPreference(long userID, long itemID, float value) throws TasteException {
    recommender.setPreference(userID, itemID, value);
  }

  
  public void removePreference(long userID, long itemID) throws TasteException {
    recommender.removePreference(userID, itemID);
  }

  
  public DataModel getDataModel() {
    return recommender.getDataModel();
  }

  
  public void refresh(Collection<Refreshable> alreadyRefreshed) {
    recommender.refresh(alreadyRefreshed);
  }

  
  public String toString() {
    return "MovieRecommender[recommender:" + recommender + ']';
  }

}
