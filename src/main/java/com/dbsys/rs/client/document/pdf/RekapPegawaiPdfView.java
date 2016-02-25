package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.connector.adapter.RekapPegawaiAdapter;

import com.dbsys.rs.client.DateUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapPegawaiPdfView extends AbstractPdfView {

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        List<RekapPegawaiAdapter> list = (List<RekapPegawaiAdapter>) model.get("list");
        Date awal = (Date) model.get("awal");
        Date akhir = (Date) model.get("akhir");
        
        Paragraph paragraph = new Paragraph();
        createTitle(paragraph);
        createContent(paragraph, awal, akhir);
        createContent(paragraph, list);

        doc.add(paragraph);

        name = String.format("rekap-barang-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP PELAYANAN PEGAWAI", fontSubTitle));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
    }

    private void createContent(Paragraph paragraph, Date awal, Date akhir) throws DocumentException {
        float columnWidths[] = {4f, 6f, 4f, 6f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);
        
        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.NO_BORDER);
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 3, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<RekapPegawaiAdapter> list) throws DocumentException {
        float columnWidths[] = {10f, 5f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "Tindakan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tarif", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Jumlah", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Sub-Total", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        String namaPegawai = "";
        Long total = 0L;
        for (RekapPegawaiAdapter pegawai : list) {
            if (!namaPegawai.equals(pegawai.getNama())) {
                if (!namaPegawai.equals("")) {
                    insertCell(table, "TOTAL", Element.ALIGN_RIGHT, 3, fontHeader, Rectangle.BOX);
                    insertCell(table, total.toString(), align, 1, fontHeader, Rectangle.BOX);
                }

                namaPegawai = pegawai.getNama();
                insertCell(table, namaPegawai, align, 4, fontHeader, Rectangle.BOX, Color.GREEN);
            }
            
            insertCell(table, pegawai.getTindakan(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pegawai.getTarif().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pegawai.getJumlah().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pegawai.getTotal().toString(), align, 1, fontContent, Rectangle.BOX);
            
            total += pegawai.getTotal();
        }

        paragraph.add(table);
    }
}
