package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.Pembayaran;
import com.dbsys.rs.lib.entity.Tagihan;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class TagihanPdfView extends  AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        Pembayaran pembayaran = (Pembayaran) model.get("pembayaran");
        Pasien pasien = pembayaran.getPasien();
        
        List<Tagihan> list = new ArrayList<>();
        for (Pelayanan pelayanan : pembayaran.getListPelayanan())
            list.add(pelayanan);
        for (Pemakaian pemakaian : pembayaran.getListPemakaian())
            list.add(pemakaian);

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, pasien);
        createContent(paragraph, list);

        doc.add(paragraph);
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("Struk Tagihan", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    @Override
    public Document newDocument() {
        return new Document(PageSize.A4);
    }

    private void createContent(Paragraph paragraph, Pasien pasien) {
        float columnWidths[] = {5f, 5f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Kode", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getKode(), align, 1, fontHeader, Rectangle.NO_BORDER);

        insertCell(table, "No. Record", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getKodePenduduk(), align, 1, fontHeader, Rectangle.NO_BORDER);

        insertCell(table, "Nama", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getNama(), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggungan", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getPenanggung().toString(), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggal Masuk", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getTanggalMasuk().toString(), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggal Keluar", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, pasien.getTanggalKeluar().toString(), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<Tagihan> list) {
        float columnWidths[] = {3f, 3f, 3f, 3f, 4f, 4f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Nama", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Unit", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tagihan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Biaya Tambahan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Keterangan", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (Tagihan tagihan : list) {
            insertCell(table, tagihan.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getNamaUnit(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getTanggal().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getTagihan().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getBiayaTambahan().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getKeterangan(), align, 1, fontContent, Rectangle.BOX);
            
            total += tagihan.getTagihan();
        }

        insertCell(table, "Total", align, 5, fontHeader, Rectangle.BOX);
        insertCell(table, total.toString(), align, 1, fontHeader, Rectangle.BOX);

        paragraph.add(table);
    }
    
    
    
}
