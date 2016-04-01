package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;

import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.entity.Pembayaran;
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
public class RekapPembayaranPdfView extends AbstractPdfView {

    private Date awal;
    private Date akhir;

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        name = String.format("rekap-tagihan-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        Paragraph paragraph = new Paragraph();

        createTitle(paragraph);
        
        awal = (Date) model.get("awal");
        akhir = (Date) model.get("akhir");
        createContentDateRange(paragraph, awal, akhir);

        List<Pembayaran> list = (List<Pembayaran>) model.get("list");
        createContent(paragraph, list);

        doc.add(paragraph);

        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP PEMBAYARAN", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }
    
    private PdfPTable createContentHead() {
        float columnWidths[] = {4f, 16f};

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);
        
        return table;
    }

    private void createContentDateRange(Paragraph paragraph, Date awal, Date akhir) throws DocumentException {
        PdfPTable table = createContentHead();
        
        insertCell(table, "Periode", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<Pembayaran> list) throws DocumentException {
        float columnWidths[] = {6f, 4f, 5f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Kode", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "No. Pasien", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Nama Pasien", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Long total = 0L;
        for (Pembayaran pembayaran : list) {
            total += pembayaran.getJumlah();
            
            insertCell(table, pembayaran.getKode(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pembayaran.getTanggal().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pembayaran.getPasien().getKode(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pembayaran.getPasien().getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, numberFormat.format(pembayaran.getJumlah()), align, 1, fontContent, Rectangle.BOX);
        }

        insertCell(table, "Total : ", Element.ALIGN_RIGHT, 4, fontContent, Rectangle.NO_BORDER);
        insertCell(table, numberFormat.format(total), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
    }

}
