package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;

import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.entity.Barang;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapBarangPdfView extends AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<Barang> list = (List<Barang>) model.get("list");
        
        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createSubTitle(paragraph);
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("rekap-barang-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP OBAT/BHP", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    private void createSubTitle(Paragraph paragraph) throws DocumentException {
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", DateUtil.getDate()), align, 3, fontContent, Rectangle.NO_BORDER);
        
        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }

    private void createContent(Paragraph paragraph, List<Barang> list) throws DocumentException {
        float columnWidths[] = {10f, 10f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Obat/BHP", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        for (Barang barang : list) {
            insertCell(table, barang.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, barang.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
        }

        paragraph.add(table);
    }
}
