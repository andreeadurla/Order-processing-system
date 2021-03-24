package Presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import BLL.ClientBLL;
import BLL.OrderBLL;
import BLL.OrderItemBLL;
import BLL.ProductBLL;
import Model.Client;
import Model.OrderClient;
import Model.OrderItem;
import Model.Product;

/**
* Clasa Printer se ocupa de generarea unor fisiere pdf
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/

public class Printer {
	
	private int nrPdfClients = 1;
	private int nrPdfProducts = 1;
	private int nrPdfOrders = 1;
	private int nrPdfReportOrders  = 1;
	
	private static final Font fontNormal = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
	private static final Font fontBold = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
	private static final Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 15, Font.NORMAL, BaseColor.BLACK);
	
	private static ClientBLL clientBLL = new ClientBLL();
	private static ProductBLL productBLL = new ProductBLL();
	private static OrderBLL orderBLL = new OrderBLL();
	private static OrderItemBLL orderItemBLL = new OrderItemBLL();
	private String dirPath;
	
	private static final Logger LOGGER = Logger.getLogger(Printer.class.getName());
	
	/**
	 * <p>Creeaza un folder in care se vor stoca toate fisierele pdf generate.</p>
	 * @throws Exception daca exista deja folder-ul in care se vor stoca fisierele pdf
	 */
	public Printer() throws Exception {
		File dir = new File("Directory_PDF");
        if (dir.mkdir())
        	dirPath = dir.getAbsolutePath();
        else {
        	throw new Exception("Failed to create directory \"Directory_PDF\"!");
        }
	}
	
	/**
	 * <p>Insereaza in document titlul dat.</p>
	 * @param document documentul in care se va face inserarea
	 * @param title titlul care va fi inserat
	 */
	private void createTitle(Document document, String title) {
        try {
        	Paragraph p = new Paragraph(title + "\n\n\n", fontTitle);
            p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Creeaza o celula in tabelul dat si adauga textul dorit in acea celula.</p>
	 * @param table tabelul in care se va face inserarea
	 * @param text textul care va fi inserat
	 * @param font font-ul cu care va fi scris textul
	 * @param align modul de aliniere al textului in celula tabelului
	 * @param colspan numarul de coloane care vor fi imbinate
	 */
	private void insertCell(PdfPTable table, String text, Font font, int align, int colspan) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		if(text.equals(" "))
			cell.setFixedHeight(5f);
		else
			cell.setFixedHeight(20f);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		table.addCell(cell);
	}
	
	/**
	 * <p>Insereaza in tabel toate atributele specifice obiectelor date.</p>
	 * @param <T> descrie tipul parametrului
	 * @param table tabelul in care se va face inserarea
	 * @param objects obiectele ale caror atribute vor fi inserate in tabel
	 */
	private <T> void printAllFields(PdfPTable table, List<T> objects) {
		for(T o: objects) {
        	for (Field field : o.getClass().getDeclaredFields()) {
    			field.setAccessible(true);
				try {
					Object value = field.get(o);
					insertCell(table, String.valueOf(value), fontNormal, Element.ALIGN_CENTER, 1);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} 
    		}
        }
	}
	
	/**
	 * <p>Creeaza un pdf in care sunt afisati toti clientii din baza de date.</p>
	 */
	public void createPdfClients() {
		Document document = new Document();
		List<Client> clients = clientBLL.findAllClients();
		
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dirPath + "\\ReportClient_" + nrPdfClients + ".pdf"));
            document.open();
            float[] columnWidths = {2f, 5f, 5f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);
            
            insertCell(table, "ID_Client", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Name", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Address", fontBold, Element.ALIGN_CENTER, 1);
            table.setHeaderRows(1);
            
            printAllFields(table, clients);
            createTitle(document, "Report Client");
            document.add(table);
            nrPdfClients ++;
            
        } catch (Exception e) {
        	LOGGER.log(Level.WARNING, "createPdfClients " + e.getMessage());
        } finally {
        	document.close();
        }
	}
	
	/**
	 * <p>Creeaza un pdf in care sunt afisate toate produsele din baza de date.</p>
	 */
	public void createPdfProducts() {
		Document document = new Document();
		List<Product> products = productBLL.findAllProducts();
		
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dirPath + "\\ReportProduct_" + nrPdfProducts + ".pdf"));
            document.open();
            float[] columnWidths = {2f, 4f, 2f, 2f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);
            
            insertCell(table, "ID_Product", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Name", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Cantity", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Price", fontBold, Element.ALIGN_CENTER, 1);
            table.setHeaderRows(1);
            
            printAllFields(table, products);
            createTitle(document, "Report Product");
            document.add(table);
            nrPdfProducts ++;
            
        } catch (Exception e) {
        	LOGGER.log(Level.WARNING, "createPdfProducts " + e.getMessage());
        } finally {
        	document.close();
        }
	}
	
	/**
	 * <p>Creeaza un pdf in care este afisata comanda unui client urmata de toate produsele aferente acelei comenzi.</p>
	 * @param order comanda care va fi procesata si afisata
	 */
	public void createPdfOrderClient(OrderClient order) {
		Document document = new Document();
		List<OrderItem> orderItems = orderItemBLL.findByIdOrder(order.getIdOrder());
		
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dirPath + "\\Order_" + order.getClientName() + "_" + nrPdfOrders + ".pdf"));
            document.open();
            float[] columnWidths = {2.7f, 2f, 2.5f, 3f, 2f, 2f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);
            
            insertCell(table, "ID_OrderItem", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "ID_Order", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "ID_Product", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Name_Product", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Cantity", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Price", fontBold, Element.ALIGN_CENTER, 1);
            table.setHeaderRows(1);
            
            insertCell(table, " ", fontNormal, Element.ALIGN_LEFT, 6);
            insertCell(table, order.toString(), fontNormal, Element.ALIGN_LEFT, 6);
            printAllFields(table, orderItems);
            
            createTitle(document, "Bill");
            document.add(table);
            nrPdfOrders ++;
            
        } catch (Exception e) {
        	LOGGER.log(Level.WARNING, "createPdfOrderClient " + e.getMessage());
        } finally {
        	document.close();
        }
	}
	
	/**
	 * <p>Creeaza un pdf in care sunt afisate toate comenzile si toate produsele aferente fiecare comenzi.</p>
	 */
	public void createPdfOrder() {
		Document document = new Document();
		List<OrderClient> orders = orderBLL.findAllOrders();
		
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dirPath + "\\ReportOrder_" + nrPdfReportOrders + ".pdf"));
            document.open();
            float[] columnWidths = {2.7f, 2f, 2.5f, 3f, 2f, 2f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);
            
            insertCell(table, "ID_OrderItem", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "ID_Order", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "ID_Product", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Name_Product", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Cantity", fontBold, Element.ALIGN_CENTER, 1);
            insertCell(table, "Price", fontBold, Element.ALIGN_CENTER, 1);
            table.setHeaderRows(1);
            
            for(OrderClient o: orders) {
            	insertCell(table, " ", fontNormal, Element.ALIGN_LEFT, 6);
            	insertCell(table, o.toString(), fontNormal, Element.ALIGN_LEFT, 6);
            	List<OrderItem> orderItems = orderItemBLL.findByIdOrder(o.getIdOrder());
                printAllFields(table, orderItems);
            }
            createTitle(document, "Report Order");
            document.add(table);
            nrPdfReportOrders ++;
            
        } catch (Exception e) {
        	LOGGER.log(Level.WARNING, "createPdfOrder " + e.getMessage());
        } finally {
        	document.close();
        }
	}
	
	/**
	 * <p>Creeaza un pdf in care se va afisa un mesaj corespunzator unei anumite comenzi</p>
	 * @param clientName numele clientului care efectueaza comanda
	 * @param message mesajul care se va afisa
	 */
	public void createPdfOrderMessage(String clientName, String message) {
		Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(dirPath + "\\Order_" + clientName + "_" + "Warning_" + nrPdfOrders + ".pdf"));
            document.open();
            createTitle(document, "Warning");
            document.add(new Phrase(message));
            nrPdfOrders ++;
            
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
        	document.close();
        }
	}
}
