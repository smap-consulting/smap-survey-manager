package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

import com.google.common.io.Files;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.condition.EvaluationContext;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.instance.InstanceInitializationFactory;
import org.javarosa.core.model.test.FormDefTest;
import org.javarosa.core.model.utils.QuestionPreloader;
import org.javarosa.core.reference.ReferenceManager;
import org.javarosa.core.reference.RootTranslator;
import org.javarosa.core.services.PrototypeManager;
import org.javarosa.core.services.storage.IStorageFactory;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.core.services.storage.util.DummyIndexedStorageUtility;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.model.xform.XFormsModule;

import org.javarosa.xform.util.XFormUtils;
import controller.FileReferenceFactory;

import controller.FormController;


public class Controller {

	private final static String t = "FormLoaderTask";
    /**
     * Classes needed to serialize objects. Need to put anything from JR in here.
     */
    public final static String[] SERIALIABLE_CLASSES = {
    		"org.javarosa.core.services.locale.ResourceFileDataSource", // JavaRosaCoreModule
    		"org.javarosa.core.services.locale.TableLocaleSource", // JavaRosaCoreModule
            "org.javarosa.core.model.FormDef",
			"org.javarosa.core.model.SubmissionProfile", // CoreModelModule
			"org.javarosa.core.model.QuestionDef", // CoreModelModule
			"org.javarosa.core.model.GroupDef", // CoreModelModule
			"org.javarosa.core.model.instance.FormInstance", // CoreModelModule
			"org.javarosa.core.model.data.BooleanData", // CoreModelModule
			"org.javarosa.core.model.data.DateData", // CoreModelModule
			"org.javarosa.core.model.data.DateTimeData", // CoreModelModule
			"org.javarosa.core.model.data.DecimalData", // CoreModelModule
			"org.javarosa.core.model.data.GeoPointData", // CoreModelModule
			"org.javarosa.core.model.data.GeoShapeData", // CoreModelModule
			"org.javarosa.core.model.data.GeoTraceData", // CoreModelModule
			"org.javarosa.core.model.data.IntegerData", // CoreModelModule
			"org.javarosa.core.model.data.LongData", // CoreModelModule
			"org.javarosa.core.model.data.MultiPointerAnswerData", // CoreModelModule
			"org.javarosa.core.model.data.PointerAnswerData", // CoreModelModule
			"org.javarosa.core.model.data.SelectMultiData", // CoreModelModule
			"org.javarosa.core.model.data.SelectOneData", // CoreModelModule
			"org.javarosa.core.model.data.StringData", // CoreModelModule
			"org.javarosa.core.model.data.TimeData", // CoreModelModule
			"org.javarosa.core.model.data.UncastData", // CoreModelModule
			"org.javarosa.core.model.data.helper.BasicDataPointer", // CoreModelModule
			"org.javarosa.core.model.data.helper.BasicDataPointer", // CoreModelModule
			"org.javarosa.core.model.Action", // CoreModelModule
			"org.javarosa.core.model.actions.SetValueAction" // CoreModelModule
    };
    
    private static final String ITEMSETS_CSV = "itemsets.csv";
    
    private static boolean isJavaRosaInitialized = false;
    /**
     * The JR implementation here does not look thread-safe or
     * like something to be invoked more than once.
     * Moving it within a critical section and a do-once guard.
     */
    private static final void initializeJavaRosa() {
    	synchronized (t) {
    		if ( !isJavaRosaInitialized ) {
	            // need a list of classes that formdef uses
	            // unfortunately, the JR registerModule() functions do more than this.
	            // register just the classes that would have been registered by:
	            // new JavaRosaCoreModule().registerModule();
	            // new CoreModelModule().registerModule();
	            // replace with direct call to PrototypeManager
	            PrototypeManager.registerPrototypes(SERIALIABLE_CLASSES);
	            new XFormsModule().registerModule();
	            isJavaRosaInitialized = true;
    		}
    	}
    }
	/*

	public static void main(String[] args) throws FileNotFoundException {
		initializeJavaRosa();
		
		//Get Demo Survey
		String filePath = "data/Demo Form.xml";
		File formXml = new File(filePath);
		String formFileName = formXml.getName().substring(0, formXml.getName().lastIndexOf("."));
		File formMediaDir = new File( formXml.getParent(), formFileName + "-media");
		
		//Load into FormDef
		FileInputStream inputStream = new FileInputStream(filePath);
        FormDef formDef = XFormUtils.getFormFromInputStream(inputStream);
        FormEntryModel formEntryModel = new FormEntryModel(formDef);
        FormEntryController formEntryController = new FormEntryController(formEntryModel);
        
        formDef.initialize(true, new InstanceInitializationFactory());
        
        // This should get moved to the Application Class
        if (ReferenceManager._().getFactories().length == 0) {
            // this is /sdcard/odk
            ReferenceManager._().addReferenceFactory(
                new FileReferenceFactory("../"));
        }

        // Set jr://... to point to /sdcard/odk/forms/filename-media/
        ReferenceManager._().addSessionRootTranslator(
            new RootTranslator("jr://images/", "jr://file/forms/" + formFileName + "-media/"));
        ReferenceManager._().addSessionRootTranslator(
                new RootTranslator("jr://image/", "jr://file/forms/" + formFileName + "-media/"));
        ReferenceManager._().addSessionRootTranslator(
            new RootTranslator("jr://audio/", "jr://file/forms/" + formFileName + "-media/"));
        ReferenceManager._().addSessionRootTranslator(
            new RootTranslator("jr://video/", "jr://file/forms/" + formFileName + "-media/"));

        
        
        FormController formController = new FormController(formMediaDir, formEntryController, null);
        
        
        
        while(!formController.currentPromptIsQuestion())
    		formController.stepToNextEvent(true);
        formController.stepToNextEvent(true);
        FormEntryPrompt fep = formController.getQuestionPrompt();

       
        IStorageFactory fact = new IStorageFactory() {
			
			@Override
			public IStorageUtility newStorage(String name, Class type) {
				return new DummyIndexedStorageUtility();
			}
		};
        StorageManager.setStorageFactory(fact);
        
        
        
        //Accessing external media (may not be needed)
        //set paths to /sdcard/odk/forms/formfilename-media/
        //String formFileName = formXml.getName().substring(0, formXml.getName().lastIndexOf("."));
        //File formMediaDir = new File( formXml.getParent(), formFileName + "-media");


        
//      //new evaluation context for function handlers
//        EvaluationContext ec = new EvaluationContext(null);
//        //ec.addFunctionHandler(null);
//        //formDef.setEvaluationContext(ec);
////        try {
////            loadExternalData(formMediaDir);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
        
        //FormInstance instance = formDef.getInstance();
        
        
        
        
        
//        FormIndex formIndex = formEntryModel.getFormIndex();
//        Boolean relevant = false;
//        while(!relevant){
//        	formIndex = formEntryModel.incrementIndex(formEntryModel.getFormIndex());
//        }
//        //FormEntryPrompt formEntryPrompt = formEntryModel.getQuestionPrompt();
//        
//        //Pull out question defs
//        Vector children = formDef.getChildren();
//        QuestionDef questionDef = (QuestionDef) children.get(0);
//        questionDef.getLabelInnerText();

        
       
        
        
        
		int i=0;
		//Validate form
		
		i++;
	} 
	*/
	static String readfile(String filePath){
		String contents = new String();
		try {
			contents = Files.toString(new File(filePath), Charset.defaultCharset());
		} catch (IOException e) {
			System.err.println("Read file failed");
			e.printStackTrace();
		}
		return contents;
	}
	
}
