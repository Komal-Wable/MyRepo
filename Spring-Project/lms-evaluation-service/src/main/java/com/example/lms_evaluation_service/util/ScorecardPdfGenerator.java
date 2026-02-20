package com.example.lms_evaluation_service.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ScorecardPdfGenerator {

    public static String generate(

            String studentName,
            String assignmentTitle,
            String courseTitle,
            Integer marks,
            String feedback,
            String grade,
            String uploadDir

    ) throws Exception {

        String fileName =
                "scorecard_" + System.currentTimeMillis() + ".pdf";

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                new FileOutputStream(uploadDir + "/" + fileName)
        );

        document.open();

        Font titleFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);

        Paragraph title =
                new Paragraph("LMS SCORECARD", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Student Name : " + studentName));
        document.add(new Paragraph("Course : " + courseTitle));
        document.add(new Paragraph("Assignment : " + assignmentTitle));
        document.add(new Paragraph("-----------------------------------"));
        document.add(new Paragraph("Marks : " + marks));
        document.add(new Paragraph("Grade : " + grade));
        document.add(new Paragraph("Feedback : " + feedback));

        document.close();

        return fileName;
    }
}
