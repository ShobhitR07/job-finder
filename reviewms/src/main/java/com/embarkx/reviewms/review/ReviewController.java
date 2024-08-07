package com.embarkx.reviewms.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService ;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        List<Review> review=reviewService.getAllReviews(companyId) ;
        if(review.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND) ;
        }else{
            return new ResponseEntity<>(review,HttpStatus.OK) ;
        }
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId,@RequestBody Review review){
        boolean ans=reviewService.addReview(companyId,review);
        if(ans){
            return new ResponseEntity<>("review posted successfully!",HttpStatus.OK) ;
        }else{
            return new ResponseEntity<>("company does not exist!",HttpStatus.NOT_FOUND) ;
        }

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){
        Review review =reviewService.getReview(reviewId) ;
        if(review==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND) ;
        }
        return new ResponseEntity<>(review,HttpStatus.OK) ;
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,@RequestBody Review review){
        boolean update= reviewService.updateReview(reviewId, review);
        if(update){
            return new ResponseEntity<>("update successful!",HttpStatus.OK) ;
        }else{
            return new ResponseEntity<>("company or review does not exist!",HttpStatus.NOT_FOUND) ;
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        boolean delete= reviewService.deleteReview( reviewId);
        if(delete){
            return new ResponseEntity<>("review deleted successfully!",HttpStatus.OK) ;
        }else{
            return new ResponseEntity<>("review not deleted!",HttpStatus.NOT_FOUND) ;
        }
    }




}
