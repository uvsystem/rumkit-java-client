package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.document.AbstractDocumentView;
import com.dbsys.rs.lib.DateUtil;
import java.sql.Date;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public abstract class AbstractPdfView extends AbstractDocumentView {
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

    protected abstract void createTitle(Paragraph paragraph) throws DocumentException;
    public abstract Document newDocument();

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
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setBorder(border);

        if("".equals(text))
            cell.setMinimumHeight(minimumCellHeight);

        table.addCell(cell);
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
}
