/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

/**
 * A skeleton implementation.
 */
public abstract class DocExportAction extends DownloadAction
{
    public static final String FILE_NOT_SUPPORTED = "file_not_supported";

    // ----------------------------------------------------------------------

    private String docType;

    // ----------------------------------------------------------------------

    public String getDocType()
    {
        return docType;
    }

    public void setDocType(String docType)
    {
        this.docType = docType;
    }
}
