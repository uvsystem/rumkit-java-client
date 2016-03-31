package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Pemakaian;
import com.dbsys.rs.client.entity.Pembayaran;
import com.dbsys.rs.client.entity.Tagihan;
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
public class PembayaranPdfView extends  AbstractPdfView {
    
    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        Pembayaran pembayaran = (Pembayaran) model.get("pembayaran");
        
        List<Tagihan> list = new ArrayList<>();
        for (Pelayanan pelayanan : pembayaran.getListPelayanan())
            list.add(pelayanan);
        for (Pemakaian pemakaian : pembayaran.getListPemakaian())
            list.add(pemakaian);

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, pembayaran);
        createContent(paragraph, list);
        createFooter(paragraph);

        doc.add(paragraph);
        
        name = String.format("pembayaran-%s", pembayaran.getKode());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("BUKTI PEMBAYARAN", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    @Override
    public Document newDocument() {
        return new Document(PageSize.A4);
    }

    private void createContent(Paragraph paragraph, Pembayaran pembayaran) {
        Pasien pasien = pembayaran.getPasien();
        
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "No. Pasien", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pasien.getKode()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "No. Pembayaran", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pembayaran.getKode()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Nama Pasien", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pasien.getNama()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tgl. Pembayaran", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pembayaran.getTanggal()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "No. Medrek", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pasien.getKodePenduduk()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggungan", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", pasien.getPenanggung()), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<Tagihan> list) {
        float columnWidths[] = {6f, 2f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Tindakan/Obat/BHP", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Biaya", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (Tagihan tagihan : list) {
            insertCell(table, tagihan.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, numberFormat.format(tagihan.getTagihan()), align, 1, fontContent, Rectangle.BOX);
            
            total += tagihan.getTagihanCounted();
        }

        insertCell(table, "Total Tagihan Pasien", Element.ALIGN_RIGHT, 2, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format(": %s", numberFormat.format(total)), align, 1, fontHeader, Rectangle.NO_BORDER);

        paragraph.add(table);
    }
}
