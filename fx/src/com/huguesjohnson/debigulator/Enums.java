/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.debigulator;

public interface Enums{
    
    public enum ArchiveType{
        Zip("Zip (.zip)"), 
        GZip("GZip (.gz)"), 
        Jar("Java Archive (.jar)"),
        SevenZip("7-Zip (.7z)");
        private final String description;
        private ArchiveType(String desc){
            description=desc;
        }
        public String getDescription(){
            return(description);
        }
        @Override
        public String toString(){
            return(description);
        }
    }
    
    public enum ActionAfterArchive{
        None("None"), 
        Verify("Verify archives"), 
        Delete("Delete source files"), 
        VerifyDelete("Delete source files after verifing");
        private final String action;
        private ActionAfterArchive(String act){
            action=act;
        }
        public String getAction(){
            return(action);
        }
        @Override
        public String toString(){
            return(action);
        }
    }
}
