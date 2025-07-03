package com.sec.movietalk.home.controller;

import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.review.ReviewReports;
import com.sec.movietalk.common.domain.comment.CommentReports;

import com.sec.movietalk.home.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/home")
    public String adminHome()
    {
        return "admin/home";
    }
    // Users
    @GetMapping("/users")
    public String users(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<User> userPage = adminService.getUserPage(page, size);
        model.addAttribute("pageData", userPage);
        return "admin/user_list";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Reviews
    @GetMapping("/reviews")
    public String reviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<Review> reviewPage = adminService.getReviewPage(page, size);
        model.addAttribute("pageData", reviewPage);
        return "admin/review_list";
    }

    @DeleteMapping("/reviews/{id}")
    public String deleteReviews(@PathVariable Long id) {
        adminService.deleteReview(id);
        return "redirect:/admin/reviews";
    }


    // Comments
    @GetMapping("/comments")
    public String comments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<Comment> commentPage = adminService.getCommentPage(page, size);
        model.addAttribute("pageData", commentPage);
        return "admin/comment_list";
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id) {
        adminService.deleteComment(id);
        return "redirect:/admin/comments";
    }

    // Review Reports
    @GetMapping("/review_reports")
    public String reviewReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<ReviewReports> reportPage = adminService.getReviewReportsPage(page, size);
        model.addAttribute("pageData", reportPage);
        return "admin/review_report_list";
    }

    @DeleteMapping("/review_reports/{id}")
    public String deleteReviewReport(@PathVariable Long id) {

        adminService.deleteReviewReport(id);
        return "redirect:/admin/review_reports";
    }



    // Comment Reports
    @GetMapping("/comment_reports")
    public String commentReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<CommentReports> reportPage = adminService.getCommentReportsPage(page, size);
        model.addAttribute("pageData", reportPage);

        return "admin/comment_report_list";
    }

    @DeleteMapping("/comment_reports/{id}")

    public String deleteCommentReport(@PathVariable Long id) {

        adminService.deleteCommentReport(id);

        return "redirect:/admin/comment_reports";
    }
}


