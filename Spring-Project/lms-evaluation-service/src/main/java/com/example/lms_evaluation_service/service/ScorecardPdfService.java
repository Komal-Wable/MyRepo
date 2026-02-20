package com.example.lms_evaluation_service.service;

import com.example.lms_evaluation_service.entity.Evaluation;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
public class ScorecardPdfService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String generateScorecard(Evaluation evaluation) {

        try {

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName =
                    UUID.randomUUID() + "_scorecard.pdf";

            String path =
                    uploadDir + File.separator + fileName;

            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("LMS SCORECARD"));
            document.add(new Paragraph("---------------------------"));
            document.add(new Paragraph("Submission ID: "
                    + evaluation.getSubmissionId()));
            document.add(new Paragraph("Marks: "
                    + evaluation.getMarks()));
            document.add(new Paragraph("Grade: "
                    + evaluation.getGrade()));
            document.add(new Paragraph("Feedback: "
                    + evaluation.getFeedback()));

            document.close();

            return fileName;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate scorecard PDF");
        }
    }
}
