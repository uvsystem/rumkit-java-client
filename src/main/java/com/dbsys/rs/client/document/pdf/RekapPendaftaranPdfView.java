package com.dbsys.rs.client.document.pdf;

import static com.dbsys.rs.client.document.pdf.AbstractPdfView.addEmptyLine;

import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Penduduk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapPendaftaranPdfView extends AbstractPdfView {

    private Date awal;
    private Date akhir;

    @Override
    public Document create(Map<String, Object> model, Document doc) throws DocumentException {
        doc.newPage();

        name = String.format("rekap-barang-%s-%s", DateUtil.getDate(), DateUtil.getTime());
        
        Paragraph paragraph = new Paragraph();

        createTitle(paragraph);
        
        awal = (Date) model.get("awal");
        akhir = (Date) model.get("akhir");
        createContentDateRange(paragraph, awal, akhir);

        List<Pasien> list = (List<Pasien>) model.get("list");
        createContent(paragraph, list);

        doc.add(paragraph);

        return doc;
    }

    @Override
    protected void createTitle(Paragraph paragraph) throws DocumentException {
        paragraph.add(new Paragraph("Rumah Sakit Umum Daerah Liun Kendage", fontTitle));
        paragraph.add(new Paragraph("REKAP PENDAFTARAN PASIEN", fontSubTitle));
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
        insertCell(table, String.format( ": %s s/d %s", awal, akhir), align, 3, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
        addEmptyLine(paragraph, 1);
    }
    
    private void createContent(Paragraph paragraph, List<Pasien> list) throws DocumentException {
        List<String> listKodeBaru = new ArrayList<>();
        List<String> listKodeLama = new ArrayList<>();
        
        float columnWidths[] = {5f, 5f, 5f, 5f, 5f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(tablePercentage);

        insertCell(table, "No. Medrek", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Nama", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tanggal", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Tujuan", align, 1, fontHeader, Rectangle.BOX);
        insertCell(table, "Keterangan", align, 1, fontHeader, Rectangle.BOX);
        table.setHeaderRows(1);

        for (Pasien pasien : list) {
            
            Penduduk penduduk = pasien.getPenduduk();
            
            insertCell(table, penduduk.getKode(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, penduduk.getNama(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pasien.getTanggalMasuk().toString(), align, 1, fontContent, Rectangle.BOX);
            insertCell(table, pasien.getTujuan().getNama(), align, 1, fontContent, Rectangle.BOX);
            
            String keterangan = "";
            Date tanggalDaftar = penduduk.getTanggalDaftar();
            if (DateUtil.between(tanggalDaftar, awal, akhir)) {
                keterangan = "BARU";

                if (! listKodeBaru.contains(penduduk.getKode())) {
                    listKodeBaru.add(penduduk.getKode());
                }
            } else {

                if (! listKodeLama.contains(penduduk.getKode())) {
                    listKodeLama.add(penduduk.getKode());
                }
            }

            insertCell(table, keterangan, align, 1, fontContent, Rectangle.BOX);
        }

        insertCell(table, "Jumlah Pasien Baru : ", Element.ALIGN_RIGHT, 4, fontContent, Rectangle.NO_BORDER);
        insertCell(table, Integer.toString(listKodeBaru.size()), align, 1, fontContent, Rectangle.NO_BORDER);
        insertCell(table, "Jumlah Pasien Lama : ", Element.ALIGN_RIGHT, 4, fontContent, Rectangle.NO_BORDER);
        insertCell(table, Integer.toString(listKodeLama.size()), align, 1, fontContent, Rectangle.NO_BORDER);
        insertCell(table, "Jumlah Pasien : ", Element.ALIGN_RIGHT, 4, fontContent, Rectangle.NO_BORDER);
        insertCell(table, Integer.toString(listKodeBaru.size() + listKodeLama.size()), align, 1, fontContent, Rectangle.NO_BORDER);

        paragraph.add(table);
    }

}
