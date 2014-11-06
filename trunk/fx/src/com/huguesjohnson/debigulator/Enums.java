/*
    Debigulator - A batch compression utility
    Copyright (C) 2003-2014 Hugues Johnson

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
