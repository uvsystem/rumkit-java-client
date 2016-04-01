package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.document.DocumentView;
import com.dbsys.rs.client.DateUtil;
import java.sql.Date;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class AbstractPdfView implements DocumentView {
    protected int fontTitleSize = 14;
    protected int fontTitleStyle = Font.BOLD;
    protected int fontTitleType = Font.TIMES_ROMAN;
    protected Font fontTitle = new Font(fontTitleType, fontTitleSize, fontTitleStyle);

    protected int fontSubtitleSize = 14;
    protected final int fontSubtitleType = Font.TIMES_ROMAN;
    protected Font fontSubTitle = new Font(fontSubtitleType, fontSubtitleSize);

    protected int fontHeaderSize = 11;
    protected int fontHeaderStyle = Font.BOLD;
    protected int fontHeaderType = Font.TIMES_ROMAN;
    protected Font fontHeader = new Font(fontHeaderType, fontHeaderSize, fontHeaderStyle);

    protected int fontContentSize = 10;
    protected int fontContentType = Font.TIMES_ROMAN;
    protected Font fontContent = new Font(fontContentType, fontContentSize);

    protected int align = Element.ALIGN_LEFT;
    protected float tablePercentage = 98f;

    protected float minimumCellHeight = 10f;
    
    protected String name;

    /**
     * Untuk format angka.
     */
    protected NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    protected abstract void createTitle(Paragraph paragraph) throws DocumentException;

    public String getName() {
        if (name == null || name.equals(""))
            name = "test";
        return name;
    }

    protected void decorateDocument(Document doc, String title) {
        doc.addAuthor("UnitedVision");
        doc.addCreationDate();
        doc.addTitle(title);
    }

    protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font, int border){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setBorder(border);

        if("".equals(text))
            cell.setMinimumHeight(minimumCellHeight);

        table.addCell(cell);
    }


    protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font, int border, Color backgroundColor){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setBorder(border);

        if("".equals(text))
            cell.setMinimumHeight(minimumCellHeight);

        table.addCell(cell);
        cell.setBackgroundColor(backgroundColor);
    }
    
    protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font, int border, float minimumCellHeight){
        this.minimumCellHeight = minimumCellHeight;
        insertCell(table, text, align, colspan, font, border);
    }

    protected static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++)
      paragraph.add(new Paragraph(" "));
    }

    protected String createTanggal(Date tanggal) {
        return DateUtil.toFormattedStringDate(tanggal, "-");
    }	
    
    protected void createFooter(Paragraph paragraph) {
        addEmptyLine(paragraph, 1);

        float columnWidths[] = {2f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Petugas", Element.ALIGN_CENTER, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, "Pasien", Element.ALIGN_CENTER, 1, fontHeader, Rectangle.NO_BORDER);
        
        for (int i = 0; i < 3; i++) {
            insertCell(table, "", align, 1, fontHeader, Rectangle.NO_BORDER);
            insertCell(table, "", align, 1, fontContent, Rectangle.NO_BORDER);
        }

        insertCell(table, "( ........................... )", Element.ALIGN_CENTER, 1, fontContent, Rectangle.NO_BORDER);
        insertCell(table, "( ........................... )", Element.ALIGN_CENTER, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
    }

    public Document newDocument() {
        return new Document(PageSize.A4);
    }

    @Override
    public String getLocation() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows"))
            return "C:/print-billing-system";
        return "~/print-billing-system";
    }
}
