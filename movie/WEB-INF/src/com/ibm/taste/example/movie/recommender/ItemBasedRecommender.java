package com.ibm.taste.example.movie.recommender;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.ibm.taste.example.movie.model.table.MovieSimilarityTable;

public class ItemBasedRecommender implements Recommender {

		  private final Recommender recommender;

		  /**
		   * @throws IOException if an error occurs while creating the {@link GroupLensDataModel}
		   * @throws TasteException if an error occurs while initializing this {@link GroupLensRecommender}
		   */
		  public ItemBasedRecommender() throws IOException, TasteException {
			  this(new MovieDataModel());
		  }

		  /**
		   * <p>Alternate constructor that takes a {@link DataModel} argument, which allows this {@link Recommender}
		   * to be used with the {@link org.apache.mahout.cf.taste.eval.RecommenderEvaluator} framework.</p>
		   *
		   * @param dataModel data model
		   * @throws TasteException if an error occurs while initializing this {@link GroupLensRecommender}
		   */
		  public ItemBasedRecommender(DataModel dataModel) throws TasteException {
			  Collection<GenericItemSimilarity.ItemItemSimilarity> correlations = MovieSimilarityTable.getAllMovieSimilarities();
			  ItemSimilarity itemSimilarity = new GenericItemSimilarity(correlations);
			  recommender = new CachingRecommender(new EmbededItemBasedRecommender(new GenericItemBasedRecommender(dataModel, itemSimilarity)));
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
		  
		  private static final class EmbededItemBasedRecommender implements Recommender {

			    private final GenericItemBasedRecommender recommender;

			    private EmbededItemBasedRecommender(GenericItemBasedRecommender recommender) {
			      this.recommender = recommender;
			    }

				public float estimatePreference(long userID, long itemID)
						throws TasteException {
					return recommender.estimatePreference(userID, itemID);
				}

				public DataModel getDataModel() {
					return recommender.getDataModel();
				}

				public List<RecommendedItem> recommend(long userID, int howMany)
						throws TasteException {
					return this.recommend(userID, howMany, null);
				}

				public List<RecommendedItem> recommend(long userID,
						int howMany, Rescorer<Long> rescorer)
						throws TasteException {
					FastIDSet itemIDs = recommender.getDataModel().getItemIDsFromUser(userID);
					return recommender.mostSimilarItems(itemIDs.toArray(), howMany, null);
				}

				public void removePreference(long userID, long itemID)
						throws TasteException {
					recommender.removePreference(userID, itemID);
					
				}

				public void setPreference(long userID, long itemID, float value)
						throws TasteException {
					recommender.setPreference(userID, itemID, value);
					
				}

				public void refresh(Collection<Refreshable> alreadyRefreshed) {
					recommender.refresh(alreadyRefreshed);
				}
			  }	  
}
