/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.io.*;
import java.security.*;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.*;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.parsers.*;

import org.qsardb.model.*;

import org.w3c.dom.*;

public class SignedZipFileInput extends ZipFileInput {

	public SignedZipFileInput(File file) throws IOException, QdbException {
		super(file);

		boolean valid = validateSignature();
		if(!valid){
			throw new QdbException("Signature is not valid");
		}
	}

	private boolean validateSignature() throws IOException, QdbException {
		Document document = readSignature();

		NodeList signatures = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if(signatures.getLength() == 0){
			return false;
		}

		try {
			XMLSignatureFactory signatureFactory = XMLSignatureFactoryUtil.getDOMInstance();

			DOMValidateContext validateContext = new DOMValidateContext(new SimpleKeySelector(), signatures.item(0));
			validateContext.setURIDereferencer(new FileInputURIDereferencer());

			XMLSignature signature = signatureFactory.unmarshalXMLSignature(validateContext);

			return signature.validate(validateContext);
		} catch(Exception e){
			throw new QdbException(e);
		}
	}

	private Document readSignature() throws IOException, QdbException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);

		InputStream is = super.getInputStream("META-INF/signature.xml");

		try {
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

			return documentBuilder.parse(is);
		} catch(Exception e){
			throw new QdbException(e);
		} finally {
			is.close();
		}
	}

	static
	private class SimpleKeySelector extends KeySelector {

		@Override
		public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
			KeyValue keyValue = (KeyValue)(keyInfo.getContent()).get(0);

			PublicKey publicKey;

			try {
				publicKey = keyValue.getPublicKey();
			} catch(KeyException ke){
				throw new KeySelectorException(ke);
			}

			return new SimpleKeySelectorResult(publicKey);
		}
	}

	static
	private class SimpleKeySelectorResult implements KeySelectorResult {

		private Key key = null;


		private SimpleKeySelectorResult(Key key){
			setKey(key);
		}

		public Key getKey(){
			return this.key;
		}

		private void setKey(Key key){
			this.key = key;
		}
	}

	private class FileInputURIDereferencer implements URIDereferencer {

		public Data dereference(URIReference reference, XMLCryptoContext context) throws URIReferenceException {
			InputStream is;

			try {
				is = getInputStream(reference.getURI());
			} catch(IOException ioe){
				throw new URIReferenceException(ioe);
			}

			return new OctetStreamData(is);
		}
	}
}