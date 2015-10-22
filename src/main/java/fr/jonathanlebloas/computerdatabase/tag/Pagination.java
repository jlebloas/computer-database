package fr.jonathanlebloas.computerdatabase.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Pagination extends SimpleTagSupport {
	private int page;
	private int pageCount;

	public void setPage(int page) {
		this.page = page;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		out.print("page=" + page + " ");
		out.print("pageCount=" + pageCount + " ");
		out.print("<ul class=\"pagination\">");

		out.print("<li><p:bar page=" + page + " size=" + getJspContext().getAttribute("size") + "/></li>");
		// 3 cases on the beginning the end and the middle
		if (page <= 3) {
			for (int i = 1; i <= 5 && i < pageCount; i++) {
				out.print("<li><a href=\"#\">" + i + "</a></li>");
			}
			out.print("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");

		} else if (page >= pageCount - 2) {
			out.print("<li><a href=\"#\" aria-label=\"Previous\"> <span	aria-hidden=\"true\">&laquo;</span></a></li>");
			for (int i = pageCount - 4; i <= pageCount; i++) {
				out.print("<li><a href=\"#\">" + i + "</a></li>");
			}

		} else {
			out.print("<li><a href=\"#\" aria-label=\"Previous\"> <span	aria-hidden=\"true\">&laquo;</span></a></li>");
			for (int i = page - 2; i <= page + 2; i++) {
				out.print("<li><a href=\"#\">" + i + "</a></li>");
			}
			out.print("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");
		}

		out.print("</ul>");
	}
}
