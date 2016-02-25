package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.StokKembali;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class StokKembaliPdfView extends  AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<StokKembali> list = (List<StokKembali>) model.get("listKembali");
        Pasien pasien = list.get(0).getPasien();

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, pasien);
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("pengembalian-%s", pasien.getKode());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("DAFTAR OBAT KEMBALI", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    @Override
    public Document newDocument() {
        return new Document(PageSize.A4);
    }

    private void createContent(Paragraph paragraph, Pasien pasien) {
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "No. Pasien", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getKode()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "No. Medrek", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getKodePenduduk()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Nama Pasien", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getNama()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggungan", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getPenanggung()), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<StokKembali> list) {
        float columnWidths[] = {8f, 6f, 3f, 3f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Obat/BHP", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Nomor Pengembalian", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Biaya", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (StokKembali stok : list) {
            Long pengembalian = stok.hitungPengembalian();
            
            insertCell(table, stok.getBarang().getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, stok.getNomor(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, stok.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pengembalian.toString(), align, 1, fontContent, Rectangle.BOX);
            
            total += pengembalian;
        }

        insertCell(table, "Total Pengembalian", Element.ALIGN_RIGHT, 3, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", total.toString()), align, 1, fontHeader, Rectangle.NO_BORDER);

        paragraph.add(table);
    }
}
