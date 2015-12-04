package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.connector.adapter.RekapStokBarangAdapter;
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
public class RekapStokPdfView extends  AbstractPdfView {
    
    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<RekapStokBarangAdapter> list = (List<RekapStokBarangAdapter>) model.get("list");
        Date awal = (Date) model.get("awal");
        Date akhir = (Date) model.get("akhir");
        
        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createSubTitle(paragraph, awal, akhir);
        createContent(paragraph, list);

        doc.add(paragraph);
        
        name = String.format("rekap-stok-%s", list.hashCode());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP STOK OBAT/BHP", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }
    
    private void createSubTitle(Paragraph paragraph, Date awal, Date akhir) {
        paragraph.add(new Paragraph(String.format("Periode : %s s/d %s", awal, akhir), fontHeader));
    }

    @Override
    public Document newDocument() {
        return new Document(PageSize.A4);
    }
    
    private void createContent(Paragraph paragraph, List<RekapStokBarangAdapter> list) {
        float columnWidths[] = {8f, 2f, 2f, 2f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Nama Obat/BHP", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah Masuk", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah Keluar", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah Pemakaian", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah Kembali", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        for (RekapStokBarangAdapter rekap : list) {
            insertCell(table, rekap.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, rekap.getJumlahMasuk().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, rekap.getJumlahKeluar().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, rekap.getJumlahPemakaian().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, rekap.getJumlahKembali().toString(), align, 1, fontContent, Rectangle.BOX);
        }

        paragraph.add(table);
    }
}
