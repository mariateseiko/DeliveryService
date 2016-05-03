/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import com.opensymphony.xwork2.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Allows a client to download a file. The file to download should be
 * provided by setting the {@code fileToDownload} variable.
 *
 * @see #setFileName(String)
 * @see #setFileToDownload(File)
 */
public abstract class DownloadAction implements Action
{
    private File fileToDownload = null;
    private InputStream fileInputStream = null;
    private String fileName = null;

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        if (fileToDownload == null || !fileToDownload.exists())
            return Action.ERROR;

        if (fileName == null)
            fileName = fileToDownload.getName();

        fileInputStream = new FileInputStream(fileToDownload);

        return Action.SUCCESS;
    }

    // ----------------------------------------------------------------------

    public File getFileToDownload()
    {
        return fileToDownload;
    }

    /**
     * Sets a file to download.
     *
     * @param fileToDownload
     *     a file to send to a client.
     */
    public void setFileToDownload(File fileToDownload)
    {
        this.fileToDownload = fileToDownload;
    }

    /**
     * Sets the name of file that should be visible for a web-client.
     *
     * @param fileName
     *     the name of the file.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    // ----------------------------------------------------------------------

    public String getFileName()
    {
        return fileName;
    }

    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }
}
