package com.example.foodies.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodies.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Model {

    List<User> userList = new LinkedList<>();
    List<Restaurant> restaurantList = new ArrayList<>();
    List<Dish> dishList = new LinkedList<>();
    List<Review> reviewList = new LinkedList<>();
    User signedUser;
    boolean signedFlag;

    public static final Model instance = new Model();



    private Model() {
        signedFlag = false;
        for(int i=1;i<11;i++){
            User user = new User("name "+i, "" + i );
            userList.add(user);
        }

        Random rand = new Random();
        for(int i=0;i<10;i++){
            for(int j=0;j<4;j++) {
                int x = Math.abs(rand.nextInt() % 10);

                if( userList.get(i).getFriendsList().size() == 0 ){
                    userList.get(i).addFriend(userList.get(x));
                    userList.get(x).addFriend(userList.get(i));
                }else if (!userList.get(i).getFriendsList().contains(userList.get(x))) {
                    userList.get(i).addFriend(userList.get(x));
                    userList.get(x).addFriend(userList.get(i));
                }
            }
        }
        Random random = new Random();
        for(int i=0;i<10;i++){
            Restaurant res = new Restaurant("Restaurant name "+i);
            for(int j=0;j<10;j++){
                Dish dish = new Dish("Dish name "+i + " " + j);
                for(int k=0;k<10;k++){
                        String rating  = Integer.toString(Math.abs((random.nextInt()%5))+1);
                        Review review = new Review(dish.getId(), res.getId(),userList.get(k).getId(),rating);
                        dish.setPrice(Integer.toString(k)+"$");
                        reviewList.add(review);
                        dish.addReview(review);
                }
                dishList.add(dish);
                res.addDish(dish);
            }

            restaurantList.add(res);
        }

    }

    //-------Getters and Setters-------//
    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }
    public List<Dish> getDishList() {
        return dishList;
    }
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }
    public List<Review> getReviewList() {
        return reviewList;
    }
    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public User getSignedUser() {
        return signedUser;
    }

    public void setSignedUser(User signedUser) {
        this.signedUser = signedUser;
    }

    public boolean isSignedFlag() {
        return signedFlag;
    }

    public void setSignedFlag(boolean signedFlag) {
        this.signedFlag = signedFlag;
    }
    //---------------------------------//
    public boolean confirmUserLogin(String name,String password){
        for (User user:userList) {
            if(user.getFirstName().equals(name) && user.getPassword().equals(password)){
                setSignedFlag(true);
                setSignedUser(user);
                return true;
            }
        }
        return false;
    }
    public Dish getDishById(String dishId){
        for(int i=0;i<dishList.size();i++){
            if(dishList.get(i).id.equals(dishId)){
                return dishList.get(i);
            }
        }
        return new Dish();
    }
    public Restaurant getRestaurantById(String id){
        for(int i=0;i<restaurantList.size();i++){
            if(restaurantList.get(i).getId().equals(id)){
                return restaurantList.get(i);
            }
        }
        return new Restaurant();
    }
    public Review getReviewById(String id){
        for(int i=0;i<reviewList.size();i++){
            if(reviewList.get(i).getId().equals(id)){
                return reviewList.get(i);
            }
        }
        return new Review();
    }
    public User getUserById(String id){
        for(int i=0;i< userList.size();i++){
            if(userList.get(i).getId().equals(id)){
                return userList.get(i);
            }
        }
        return new User();
    }

    public void addReview(Review review){
        getUserById(review.getUserId()).addReview(review);
        getDishById(review.getDishId()).addReview(review);
    }
    public void addDish(Dish dish){
      getRestaurantById(dish.getRestaurantId()).addDish(dish);
      dishList.add(dish);
    }
    public void addRestaurant(Restaurant restaurant){
        restaurantList.add(restaurant);
    }
    public void addUser(User user){
        userList.add(user);
    }

    public void deleteReview(Review review){
        getDishById(review.getDishId()).deleteReview(review);       // remove from the dish's review list
        getUserById(review.getUserId()).deleteReview(review);       // remove the review from the user's review list
        reviewList.remove(review);
    }
    public void deleteDish(Dish dish){
        int size = dish.getReviewList().size();
        for(int i=0;i<size;i++){                   // remove the reviews on this dish from each users review list
            String user = dish.getReviewList().get(i).getUserId();
            int size2 = userList.size();
            for(int j=0; j<size2;j++){
                if(userList.get(j).getId().equals(user)){
                    userList.get(j).deleteReview(dish.getReviewList().get(i));
                    break;
                }
            }
        }
        size = restaurantList.size();
        for(int i=0;i<size;i++){                  // remove the dish from the restautant's dish list
            String restaurant = dish.getRestaurantId();
            if(restaurantList.get(i).getId().equals(restaurant)){
                restaurantList.get(i).deleteDish(dish);
                break;
            }
        }
        dishList.remove(dish);
    }
    public void deleteRestaurant(Restaurant restaurant){
        int size = restaurant.getDishList().size();
        for(int i=0;i<size;i++){                  // for each restaurant's dish
            int size2 = restaurant.getDishList().get(i).getReviewList().size();
            for(int j=0;j<size2;j++) {            // remove all dish's reviews from their user's review list than delete review
                int size3 = userList.size();
                for (int k = 0; k < size3;k++) {
                    if(restaurant.getDishList().get(i).getReviewList().get(j).userId.equals(userList.get(k).getId())){
                        userList.get(k).deleteReview(restaurant.getDishList().get(i).getReviewList().get(j));
                        reviewList.remove(restaurant.getDishList().get(i).getReviewList().get(j));
                    }
                }
            }
            dishList.remove(restaurant.getDishList().get(i));  // remove dishs
        }
        restaurantList.remove(restaurant); // remove restaurant
    }
    public void deleteUser(User user){
        int size = user.getFriendsList().size(); // remove user from all of his friend's users list
        for(int i=0;i<size;i++){
            user.getFriendsList().get(i).getFriendsList().remove(user);
        }
        size = user.getReviewList().size();  // remove all user's reviews from their dishes
        for(int i=0;i<size;i++){
            int size2 =dishList.size();
            String dish = user.getReviewList().get(i).getDishId();
            for(int j=0;j<size2;j++) {
                if(dishList.get(j).getId().equals(dish)){
                    dishList.get(j).deleteReview(user.getReviewList().get(i));
                }
            }
        }
        userList.remove(user); // delete from Model user list
    }

    public List<User> getAllUsersThatHaveReviewsOnRestaurantByRestaurantId(String restaurantId){
        List<User> result = new LinkedList<>();
        for(int i=0;i<reviewList.size();i++){
            if(reviewList.get(i).getRestaurantId().equals(restaurantId) && !result.contains(getUserById(reviewList.get(i).getUserId()))){
                result.add(getUserById(reviewList.get(i).getUserId()));

            }
        }
        return result;
    }
    public List<Restaurant> getAllRestaurantsThatUserHasReviewsOnByUserId(String userId){
        List<Restaurant> result = new LinkedList<>();
        for(int i=0;i<reviewList.size();i++){
            if(reviewList.get(i).getUserId().equals(userId) && !result.contains(getRestaurantById(reviewList.get(i).getRestaurantId()))){
                result.add(getRestaurantById(reviewList.get(i).getRestaurantId()));

            }
        }
        return result;
    }
    public List<Dish> getAllDishesThatTheUserHasAReviewedOnInThisRestaurantByUserIdAndRestaurantId(String userId,String restaurantId){
        List<Dish> result = new LinkedList<>();
        for(int i=0;i<reviewList.size();i++){
            if(reviewList.get(i).getUserId().equals(userId) && reviewList.get(i).getRestaurantId().equals(restaurantId) && !result.contains(getDishById(reviewList.get(i).getDishId()))){
                result.add(getDishById(reviewList.get(i).getDishId()));
            }
        }
        return result;
    }
    public Review getReviewOnDishByDishIdAndUserId(String dishId,String userId){

        for(int i=0;i<reviewList.size();i++){
            if(reviewList.get(i).getDishId().equals(dishId) && reviewList.get(i).getUserId().equals(userId)){
                return reviewList.get(i);
            }
        }
        return reviewList.get(0);
    }
    public String getRestaurantIdByName(String resName){
        for(int i=0;i<restaurantList.size();i++){
            if(restaurantList.get(i).getName().equals(resName)){
                return restaurantList.get(i).getId();
            }
        }

        return "No Such Restaurant";
    }
    public String getDishIdByRestaurantIdAndDishName(String resId,String dishName){
        Restaurant restaurant = getRestaurantById(resId);
        List<Dish> dishl = restaurant.getDishList();
        for(int i=0;i<dishl.size();i++){
            if(dishl.get(i).getName().equals(dishName)){
                return dishl.get(i).getId();
            }
        }
        return "No Such Dish";
    }
    public List<Review> getAllFriendsReviewsOnDishByDishIdAndUserId(String dishId,String userId){
        Dish dish = getDishById(dishId);
        List<User> friends = getUserById(userId).getFriendsList();
        List<Review> reviews =  new LinkedList<>();
        for(int i=0;i<dish.getReviewList().size();i++){
            for(int j=0;j<friends.size();j++){
                if(dish.reviewList.get(i).getUserId().equals(friends.get(j).getId())){
                    reviews.add(dish.reviewList.get(i));
                }
            }
        }
        return reviews;
    }

    public Integer getNumOfFriendsVisitedInRestaurant(String restaurantID){
        int count=0;
        List<User> myFriends=getSignedUser().getFriendsList();
        for (User friend: myFriends) {
            for(Review rev: reviewList){
                if(rev.restaurantId.equals(restaurantID) && friend.getReviewList().contains(rev)){
                    count++;
                }
            }
        }
        return count;
    }


//    public String getRandomFriendNameVisitedInRestaurant(String restaurantID){
//        List <User> myFriends=getSignedUser().getFriendsList();
//        Set <User> myFriendsWithReview=new HashSet<User>();
////        List <User> myFriendsWithReview=new LinkedList<>();
//        for(User friend:myFriends) {
//            for (Review rev : reviewList) {
//                if (rev.restaurantId.equals(restaurantID) && friend.getReviewList().contains(rev)) {
//                    myFriendsWithReview.add(friend);
//                }
//           }
//        }
//       return null;
//    }
    public void setStarByRating(String ratingVal, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5, TextView rateTv){

        if(!ratingVal.equals("No rating yet")){
            rateTv.setText("");
            float rate =Float.parseFloat(ratingVal);
            if(rate==0.5){
                star1.setImageResource(R.drawable.halfstar);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==1){
                star1.setImageResource(R.drawable.star);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);

            }
            else if(rate==1.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.halfstar);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==2){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==2.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.halfstar);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==3){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==3.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.halfstar);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==4){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==4.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.halfstar);
            }
            else if(rate==5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);

            }


        }
        else{
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
            star4.setVisibility(View.INVISIBLE);
            star5.setVisibility(View.INVISIBLE);
        }

    }


}
