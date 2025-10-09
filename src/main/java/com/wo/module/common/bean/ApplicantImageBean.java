package com.wo.module.common.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class ApplicantImageBean implements Serializable{
	
	private static final long serialVersionUID = 2602071144104568595L;
	@SuppressWarnings("unused")
	private StreamedContent image;
	private Blob blobImage;

	public void setImageStream(InputStream is, String contentType) {
		this.image = new DefaultStreamedContent(is, contentType);
	}

	public void setImageStream(Blob blob) {
		if (blob != null) {
			blobImage = blob;
			try {
				MagicMatch match = Magic.getMagicMatch(blob.getBytes(1, 10));
				this.image = new DefaultStreamedContent(blob.getBinaryStream(),
						match.getMimeType());
			} catch (SQLException sx) {
				sx.printStackTrace();
			} catch (MagicException mx) {
				mx.printStackTrace();
			} catch (MagicMatchNotFoundException nx) {
				nx.printStackTrace();
			} catch (MagicParseException px) {
				px.printStackTrace();
			}
		}
	}
	
	public void setImageStream(byte[] content, String contentType) {
		this.image = new DefaultStreamedContent(new ByteArrayInputStream(content),
				contentType);
		
	}

	public StreamedContent getImage() {
		if (blobImage != null) {
			try {
				MagicMatch match = Magic.getMagicMatch(blobImage.getBytes(1, 10));
				return new DefaultStreamedContent(blobImage.getBinaryStream(),
						match.getMimeType());
			} catch (SQLException sx) {
				sx.printStackTrace();
			} catch (MagicException mx) {
				mx.printStackTrace();
			} catch (MagicMatchNotFoundException nx) {
				nx.printStackTrace();
			} catch (MagicParseException px) {
				px.printStackTrace();
			}
		}
		
		return null;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public Blob getBlobImage() {
		return blobImage;
	}

	public void setBlobImage(Blob blobImage) {
		this.blobImage = blobImage;
	}
}
