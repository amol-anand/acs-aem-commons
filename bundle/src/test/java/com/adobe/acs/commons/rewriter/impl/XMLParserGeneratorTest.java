/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.adobe.acs.commons.rewriter.impl;

import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

@RunWith(MockitoJUnitRunner.class)
public class XMLParserGeneratorTest {

    @Mock
    private ContentHandler contentHandler;
    
    @Captor ArgumentCaptor<Attributes> attributesCaptor;

    @Test
    public void test() throws Exception {
        
        XMLParserGeneratorFactory factory = new XMLParserGeneratorFactory();
        XMLParserGenerator generator = (XMLParserGenerator) factory.createGenerator();
        generator.setContentHandler(contentHandler);
        
        PrintWriter printWriter = generator.getWriter();
        printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printWriter.println("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"></fo:root>");
        
        generator.finished();
        
        verify(contentHandler).startDocument();
        verify(contentHandler).setDocumentLocator((Locator) anyObject());
        verify(contentHandler).startPrefixMapping("fo", "http://www.w3.org/1999/XSL/Format");

        verify(contentHandler).startElement(eq("http://www.w3.org/1999/XSL/Format"), eq("root"), eq("fo:root"), attributesCaptor.capture());
        
        verify(contentHandler).endElement("http://www.w3.org/1999/XSL/Format", "root", "fo:root");
        
        verify(contentHandler).endPrefixMapping("fo");
        verify(contentHandler).endDocument();
        verifyNoMoreInteractions(contentHandler);
    }
}
