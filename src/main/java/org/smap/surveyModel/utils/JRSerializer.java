package org.smap.surveyModel.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.odk.FileUtils;
import org.smap.surveyModel.SurveyModel;

public class JRSerializer implements Serializable{
	
	private String surveyDefXml;
	private String surveyInstanceXml;
	private String serializedTreeRef;
	private int localIndex;
	
	public JRSerializer(String surveyDefXml, String surveyInstanceXml, FormIndex index){
		this.surveyDefXml=surveyDefXml;
		this.surveyInstanceXml=surveyInstanceXml;
		this.localIndex = index.getLocalIndex();
		this.serializedTreeRef = serializeTreeRef(index.getReference());
	}

	public static String serializeTreeRef(TreeReference treeRef){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        writeTreeRefToOutputStream(treeRef,dos);
		return new String(baos.toByteArray(), Charset.defaultCharset());
	}
	
	private static void writeTreeRefToOutputStream(TreeReference treeRef, DataOutputStream dos){
		try {
			treeRef.writeExternal(dos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static TreeReference deserializeTreeRef(int localIndexString, String serialzedRef){
		TreeReference resumedTreeRef = new TreeReference();
		readStringIntoTreeReference(resumedTreeRef, serialzedRef);
		return resumedTreeRef;
	}
	
	private static void readStringIntoTreeReference(TreeReference treeRef, String serialzedTreeRef){
		try {
			treeRef.readExternal(FileUtils.convertStringToDataInputStream(serialzedTreeRef), 
					ExtUtil.defaultPrototypes());
		} catch (IOException | DeserializationException e) {
			e.printStackTrace();
		}
	}
	
	public static SurveyModel deserializeSurveyModel(String serializedModel){
		JRSerializer serializer = getJRSerializerFromSerializedModel(serializedModel);
		TreeReference treeRef = deserializeTreeRef(serializer.localIndex,serializer.serializedTreeRef);
		FormIndex index = new FormIndex(serializer.localIndex, treeRef);
		return SurveyModel.resumeSurveyModel(serializer.surveyDefXml,serializer.surveyInstanceXml,index);
	}
	
	private static JRSerializer getJRSerializerFromSerializedModel(String serializedModel){
		try {
			return (JRSerializer) Serializer.fromString(serializedModel);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializeSurveyModel(SurveyModel model){
		JRSerializer serializer = new JRSerializer(model.getInitialFormDefXML(), model.getAnsweredXML(), model.getCurrentIndex());
		try {
			return Serializer.toString(serializer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
