package com.dragi.finance_manager.utils;

import com.dragi.finance_manager.cv.CV;
import com.dragi.finance_manager.cv.Education;
import com.dragi.finance_manager.cv.Experience;
import com.dragi.finance_manager.cv.SocialLink;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class PDFUtils {

    public static byte[] createCVPDF(CV cv) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Load the HTML template
            File htmlTemplateFile = new ClassPathResource("templates/cv_template.html").getFile();
            String htmlTemplate = new String(Files.readAllBytes(htmlTemplateFile.toPath()));

            // Replace placeholders with CV data
            String htmlContent = htmlTemplate
                    .replace("{{fullName}}", cv.getFullName())
                    .replace("{{title}}", "Title")
                    .replace("{{email}}", cv.getEmail())
                    .replace("{{phoneNumber}}", cv.getPhoneNumber())
                    .replace("{{address}}", cv.getAddress())
                    .replace("{{summary}}", cv.getSummary())
                    .replace("{{skills}}", formatList(cv.getSkills()))
                    .replace("{{languages}}", formatList(cv.getLanguages()))
                    .replace("{{profileImage}}", cv.getProfileImage() == null ? "" : cv.getProfileImage())
                    .replace("{{socialLinks}}", formatSocialLinks(cv.getSocialLinks()))
                    .replace("{{experiences}}", formatExperiences(cv.getExperiences()))
                    .replace("{{educations}}", formatEducations(cv.getEducations()));

            // Load the CSS file
            File cssFile = new ClassPathResource("static/cv_styles.css").getFile();
            String cssPath = cssFile.toURI().toString();

            // Append the CSS reference to the HTML content
            htmlContent = htmlContent.replace("<link rel=\"stylesheet\" href=\"cv_styles.css\">",
                    "<link rel=\"stylesheet\" href=\"" + cssPath + "\">");

            // Convert HTML to PDF
            HtmlConverter.convertToPdf(new ByteArrayInputStream(htmlContent.getBytes()), out);

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF for CV: " + cv.getId(), e);
        }
    }

    // Helper to format a list of strings
    private static String formatList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append("<li>").append(item).append("</li>");
        }
        return sb.toString();
    }

    // Helper to format experiences
    private static String formatExperiences(List<Experience> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return "<p>No experiences available.</p>";
        }
        StringBuilder sb = new StringBuilder();
        for (Experience exp : experiences) {
            sb.append("<article class='experience'>")
                    .append("<h4>").append(exp.getDescription()).append(" at ").append("</h4>")
                    .append("<h4>").append(exp.getPosition()).append(" at ").append(exp.getCompany()).append("</h4>")
                    .append("<p><em>").append(exp.getStartDate()).append(" - ").append(exp.getEndDate()).append("</em></p>")
                    .append("<ul>");
            sb.append("</ul></article>");
        }
        return sb.toString();
    }

    // Helper to format educations
    private static String formatEducations(List<Education> educations) {
        if (educations == null || educations.isEmpty()) {
            return "<p>No education information available.</p>";
        }
        StringBuilder sb = new StringBuilder();
        for (Education edu : educations) {
            sb.append("<article class='education'>")
                    .append("<h4>").append(edu.getDegree()).append("</h4>")
                    .append("<p><strong>Institution:</strong> ").append(edu.getInstitution()).append("</p>")
                    .append("<p><em>").append(edu.getStartDate()).append(" - ").append(edu.getEndDate()).append("</em></p>")
                    .append("</article>");
        }
        return sb.toString();
    }

    // Helper to format social links
    private static String formatSocialLinks(List<SocialLink> links) {
        if (links == null || links.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SocialLink link : links) {
            sb.append("<a href='").append(link.getUrl()).append("' target='_blank' title='")
                    .append(link.getPlatform()).append("'>")
                    .append("<i class='").append(link.getIconClass()).append("'></i></a>");
        }
        return sb.toString();
    }
}