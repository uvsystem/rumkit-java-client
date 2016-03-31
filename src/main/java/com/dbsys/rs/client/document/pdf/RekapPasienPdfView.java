package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;

import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.entity.Pasien;
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
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapPasienPdfView extends AbstractPdfView {

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP PASIEN", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<Pasien> list = (List<Pasien>) model.get("list");
        String nomorMedrek = (String) model.get("nomor");
        
        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        
        if (nomorMedrek != null && !nomorMedrek.equals("")) {
            createContentNomorMedrek(paragraph, nomorMedrek);
        } else {
            Date awal = (Date) model.get("awal");
            Date akhir = (Date) model.get("akhir");

            createContentDateRange(paragraph, awal, akhir);
        }
        
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("rekap-pasien-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        return doc;
    }
    
    private PdfPTable createContentHead(Paragraph paragraph) {
        float columnWidths[] = {4f, 16f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);
        
        return table;
    }

    private void createContentDateRange(Paragraph paragraph, Date awal, Date akhir) throws DocumentException {
        PdfPTable table = createContentHead(paragraph);
        
        insertCell(table, "Periode", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 3, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }

    private void createContentNomorMedrek(Paragraph paragraph, String nomorMedrek) throws DocumentException {
        PdfPTable table = createContentHead(paragraph);
        
        insertCell(table, "No. Mederk", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", nomorMedrek), align, 3, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<Pasien> list) throws DocumentException {
        float columnWidths[] = {5f, 5f, 10f, 5f, 5f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "No. Tagihan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "No. Medrek", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Nama", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tagihan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Pembayaran", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tunggakan", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Long totalTunggakan = 0L;
        for (Pasien pasien : list) {
            insertCell(table, pasien.getKode(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pasien.getKodePenduduk(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pasien.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pasien.getTanggalMasuk().toString(), align, 1, fontContent, Rectangle.BOX);
            
            Long totalTagihan = 0L;
            if ( pasien.getTotalTagihan() != null)
                totalTagihan = pasien.getTotalTagihan();
            insertCell(table, numberFormat.format(totalTagihan), align, 1, fontContent, Rectangle.BOX);

            Long cicilan = 0L;
            if ( pasien.getCicilan() != null)
                cicilan = pasien.getCicilan();
            insertCell(table, numberFormat.format(cicilan), align, 1, fontContent, Rectangle.BOX);

            Long tunggakan = totalTagihan - cicilan;
            insertCell(table, numberFormat.format(tunggakan), align, 1, fontContent, Rectangle.BOX);
            
            totalTunggakan += tunggakan;
        }

        insertCell(table, "Total Tunggakan : ", align, 6, fontContent, Rectangle.NO_BORDER);
        insertCell(table, numberFormat.format(totalTunggakan), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
    }

}
