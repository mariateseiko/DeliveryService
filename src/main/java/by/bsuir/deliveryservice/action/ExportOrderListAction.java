/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.OrderListExportService;
import by.bsuir.deliveryservice.service.impl.export.OrderListExportServiceFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

public class ExportOrderListAction extends DocExportAction
{
    /*
     * The ID of a courier to generate an order list for.
     */
    private Long courierId;

    /*
     * The service instance.
     */
    private static final OrderListExportService service =
            OrderListExportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        DocFormat docFormat;

        try {
            docFormat = DocFormat.valueOf(docType);
        } catch (Exception e) {
            throw new IllegalArgumentException("unknown file format", e);
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");

        File file = service.exportToFile(docFormat, u.getId());
        setFileToDownload(file);

        return super.execute();
    }

    // ----------------------------------------------------------------------

    public Long getCourierId()
    {
        return courierId;
    }

    public void setCourierId(Long courierId)
    {
        this.courierId = courierId;
    }
}
