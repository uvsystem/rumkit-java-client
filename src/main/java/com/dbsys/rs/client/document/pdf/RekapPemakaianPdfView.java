package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.connector.adapter.RekapTagihanAdapter;
import com.dbsys.rs.client.DateUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class RekapPemakaianPdfView extends  AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<RekapTagihanAdapter> list = (List<RekapTagihanAdapter>) model.get("list");
        Date awal = (Date) model.get("awal");
        Date akhir = (Date) model.get("akhir");

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, awal, akhir);
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("rekap-pemakaian-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP PEMAKAIAN OBAT/BHP", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    private void createContent(Paragraph paragraph, Date awal, Date akhir) {
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Periode", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 3, fontContent, Rectangle.NO_BORDER);
        
        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<RekapTagihanAdapter> list) {
        float columnWidths[] = {10f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Obat / BHP", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tagihan", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (RekapTagihanAdapter tagihan : list) {
            insertCell(table, tagihan.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, numberFormat.format(tagihan.getTagihan()), align, 1, fontContent, Rectangle.BOX);
            
            total += tagihan.getTagihan();
        }

        insertCell(table, "Total", Element.ALIGN_RIGHT, 2, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": Rp %s", numberFormat.format(total)), align, 1, fontHeader, Rectangle.NO_BORDER);

        paragraph.add(table);
    }
}
