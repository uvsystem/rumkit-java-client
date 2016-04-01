package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;
import com.dbsys.rs.client.entity.Pasien;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class KartuPasienPdfView extends AbstractPdfView {

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        Font fTitle = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
        
        paragraph.add(new Paragraph("RSU LIUNKENDAGE", fTitle));
        paragraph.add(new Paragraph("Kartu Tagihan Pasien", fTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
    }

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        Pasien pasien = (Pasien) model.get("pasien");

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, pasien);

        doc.add(paragraph);

        name = String.format("pasien-%s", pasien.getKode());
        
        return doc;
    }

    @Override
    public Document newDocument() {
        int margin = 2;
        return new Document(PageSize.A8.rotate(), margin, margin, margin, margin);
    }

    private void createContent(Paragraph paragraph, Pasien pasien) {
        Font font = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
        
        float columnWidths[] = {10f, 15f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Nomor", align, 1, font, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getKode()), align, 1, font, Rectangle.NO_BORDER);

        insertCell(table, "No. Medrek", align, 1, font, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getKodePenduduk()), align, 1, font, Rectangle.NO_BORDER);

        insertCell(table, "Nama Pasien", align, 1, font, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getNama()), align, 1, font, Rectangle.NO_BORDER);

        insertCell(table, "Tanggungan", align, 1, font, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getPenanggung()), align, 1, font, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }

}
