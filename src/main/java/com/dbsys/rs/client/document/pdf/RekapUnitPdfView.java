package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;
import com.dbsys.rs.connector.adapter.RekapUnitAdapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
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
public class RekapUnitPdfView extends  AbstractPdfView {
    
    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<RekapUnitAdapter> list = (List<RekapUnitAdapter>) model.get("list");
        Date awal = (Date) model.get("awal");
        Date akhir = (Date) model.get("akhir");
        
        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createSubTitle(paragraph, awal, akhir);
        createContent(paragraph, list);

        doc.add(paragraph);
        
        name = String.format("rekap-unit-%s", list.hashCode());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP TAGIHAN", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }
    
    private void createSubTitle(Paragraph paragraph, Date awal, Date akhir) {
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Periode", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 3, fontContent, Rectangle.NO_BORDER);
        
        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }

    @Override
    public Document newDocument() {
        return new Document(PageSize.A4);
    }
    
    private void createContent(Paragraph paragraph, List<RekapUnitAdapter> list) {
        float columnWidths[] = {6f, 2f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Nama Unit", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah Pelayanan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Total Tagihan", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (RekapUnitAdapter rekap : list) {
            insertCell(table, rekap.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, rekap.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            
            Long t = rekap.getTotal();
            if (rekap.getNama().equals("Intensive Care Unit"))
                t *= 2;
            
            insertCell(table, numberFormat.format(t), align, 1, fontContent, Rectangle.BOX);
            
            total += rekap.getTotal();
        }

        insertCell(table, "Total Tagihan", Element.ALIGN_RIGHT, 2, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": Rp %s", numberFormat.format(total)), align, 1, fontHeader, Rectangle.NO_BORDER);

        paragraph.add(table);
    }
}
