/**
 * Copyright 2016.
 *
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
 */
package es.tid.keyserver.https.protocol;

import org.json.simple.JSONObject;

/**
 * Class for JSON manipulation.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
class ResponseJSON {
    /**
     * OUTPUT - Constant fields for the output JSON attributes.
     */
    protected static final String OUTPUT = "output";
    /**
     * ERROR - Constant fields for the output JSON attributes.
     */
    protected static final String ERROR = "error";
    
    /**
     * JSON Object for the output data
     */
    private final JSONObject outputData;
    
    /**
     * Constructor with data from JSON "output data" field as input parameter.
     * @param type Specified the JSON Output type (OUTPUT / ERROR).
     * @param data This field contains the data associated with JSON "output data" field.
     */
    public ResponseJSON(String type, String data){
        outputData = new JSONObject();
        setOutputData(type, data);
    }
    
    /**
     * Set data to an specific label. 
     * @param label JSON Label value.
     * @param data JSON Data associated to the label. 
     */
    protected final void setOutputData(String label, String data){
        outputData.put(label, data);
    }
    
    /**
     * This method returns an JSON as String.
     * @return JSON as string.
     */
    @Override
    public String toString(){
        String output = outputData.toJSONString();
        return output.replaceAll("\\\\", "");
    }
}
