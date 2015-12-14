package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.connector.adapter.RekapTagihanAdapter;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Pasien;
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
public class TagihanPdfView extends  AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        Pasien pasien = (Pasien) model.get("pasien");
        List<RekapTagihanAdapter> list = (List<RekapTagihanAdapter>) model.get("listTagihan");

        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, pasien);
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("tagihan-%s", pasien.getKode());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("DAFTAR TAGIHAN", fontSubTitle));
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

        insertCell(table, "Tanggungan", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getPenanggung()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "No. Medrek", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getKodePenduduk()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggal Masuk", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getTanggalMasuk()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Nama Pasien", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getNama()), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "Tanggal Keluar", Element.ALIGN_RIGHT, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", pasien.getTanggalKeluar()), align, 1, fontContent, Rectangle.NO_BORDER);

        int umur = DateUtil.calculate(pasien.getTanggalLahir(), DateUtil.getDate()) / 365;
        insertCell(table, "Umur", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", umur), align, 1, fontContent, Rectangle.NO_BORDER);

        insertCell(table, "", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, "", align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<RekapTagihanAdapter> list) {
        float columnWidths[] = {8f, 5f, 4f, 4f, 4f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Tagihan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Nama Unit", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tagihan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Penanggung", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        Float total = 0F;
        for (RekapTagihanAdapter tagihan : list) {
            insertCell(table, tagihan.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getUnit(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getTagihan().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, tagihan.getPenanggung().toString(), align, 1, fontContent, Rectangle.BOX);
            
            total += tagihan.getTagihan();
        }

        insertCell(table, "Sub-Total", Element.ALIGN_RIGHT, 4, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s", total.toString()), align, 1, fontHeader, Rectangle.NO_BORDER);

        paragraph.add(table);
    }
}
