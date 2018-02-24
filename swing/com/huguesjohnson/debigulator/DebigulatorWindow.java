/*
Debigulator - A batch compression utility
DebigulatorWindow.java - Main window for program
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.huguesjohnson.BatchCompressionThreadParameters;
import com.huguesjohnson.fileFilter.AudioFileFilter;
import com.huguesjohnson.fileFilter.DirectoryFileFilter;
import com.huguesjohnson.fileFilter.GameRomzFilter;
import com.huguesjohnson.fileFilter.MicrosoftOfficeFilter;
import com.huguesjohnson.fileFilter.NonArchivedFileFilter;
import com.huguesjohnson.fileFilter.SourceCodeFileFilter;
import com.huguesjohnson.fileFilter.VideoFileFilter;
import com.huguesjohnson.ui.NoFrillsJFileChooser;

/** 
 * DebigulatorWindow main user interface for Debigulator.
 * @author Hugues Johnson
 */
public abstract class DebigulatorWindow extends JFrame implements ActionListener{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=-4601583272505113359L;
	/* progress bar for window */
    private JProgressBar progressBar;
    /* file browser to chose files to archive */
    private NoFrillsJFileChooser fileBrowser;
    /* file browser to chose output directory */
    private NoFrillsJFileChooser outputDirectoryBrowser;
    /* list of files to archive */
    private JList fileList;
    /* combo box to choose type of archive to create */
    private JComboBox archiveTypeComboBox;
    /* combo box to choose action after creating archive */
    private JComboBox actionComboBox;
    /* checkbox for whether or not to sync the output directory to the source directory */
    private JCheckBox syncToSourceDirCheckBox;
    
    /** 
     * Creates a new DebigulatorWindow.
     */
    public DebigulatorWindow(){
        super(DebigulatorConstants.PROGRAM_TITLE);
        /* add listener for close event */
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){actionPerformed(new ActionEvent(this,0,DebigulatorConstants.ACTION_EXIT));}});
   }
    
    /** 
     * Loads components into the window.
     */
    public void initialize(){
        //TODO - custom icon for the window
        /* add components */
        this.getContentPane().setLayout(new BorderLayout());
        this.addMenu();
        this.addToolbar();
        this.addCenterPanel();
        this.addProgressBar();
        /* pack the window */
        this.pack();
        /* center the window */
        Dimension screenDimension=Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle windowDimension=this.getBounds();
        this.setLocation((screenDimension.width-windowDimension.width)/2,(screenDimension.height-windowDimension.height)/2);        
        this.setResizable(true);
    }
    
    /* adds the menu */
    private void addMenu(){
        JMenuBar menuBar=new JMenuBar();
        /* Debigulator menu */
        JMenu menu=new JMenu("Debigulator");
        menu.setMnemonic(KeyEvent.VK_D);
        /* new session */
        JMenuItem menuItem=new JMenuItem(DebigulatorConstants.ACTION_NEWSESSION);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/new.gif")));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_NEWSESSION);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* open session */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_OPENSESSION);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/open.gif")));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_OPENSESSION);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* save session */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_SAVESESSION);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/save.gif")));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_SAVESESSION);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* save session as */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_SAVESESSIONAS);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/save_as.gif")));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_SAVESESSIONAS);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* create archives */
        menu.addSeparator();
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_CREATEARCHIVES);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/run.gif")));
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_CREATEARCHIVES);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* advanced settings */
        menu.addSeparator();
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_ADVANCEDSETTINGS);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/options.gif")));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_ADVANCEDSETTINGS);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* add window style menu */
//TODO - if file browse panels are correctly implemented turn this feature back on
//        menu.addSeparator();
//        JMenu subMenu=new JMenu("Window Style");
//        subMenu.setMnemonic(KeyEvent.VK_W);
//        menu.add(subMenu);
//        LookAndFeelInfo lfi[]=UIManager.getInstalledLookAndFeels();
//        int length=lfi.length;
//        for(int index=0;index<length;index++){
//            String name=lfi[index].getName();
//            String className=lfi[index].getClassName();
//            menuItem=new JMenuItem(className);
//            menuItem.setActionCommand(DebigulatorConstants.ACTION_CHANGESTYLE);
//            menuItem.addActionListener(this);
//            subMenu.add(menuItem); 
//        }
        /* exit */
        menu.addSeparator();
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_EXIT);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/exit.gif")));
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_EXIT);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        /* Help menu */
        menu=new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        /* help index */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_HELPINDEX);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/faq.gif")));
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_HELPINDEX);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* API */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_HELPAPI);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/api.gif")));
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_HELPAPI);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        /* About */
        menuItem=new JMenuItem(DebigulatorConstants.ACTION_HELPABOUT);
        menuItem.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/about.gif")));
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setActionCommand(DebigulatorConstants.ACTION_HELPABOUT);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }
    
    /* adds the toolbar */
    private void addToolbar(){
        JToolBar toolbar=new JToolBar();
        /* new button */
        JButton button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/new.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_NEWSESSION);
        button.addActionListener(this);
        toolbar.add(button);
        /* open button */
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/open.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_OPENSESSION);
        button.addActionListener(this);
        toolbar.add(button);
        /* save button */
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/save.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_SAVESESSION);
        button.addActionListener(this);
        toolbar.add(button);
        /* save as button */
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/save_as.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_SAVESESSIONAS);
        button.addActionListener(this);
        toolbar.add(button);
        /* create archives button */
        toolbar.addSeparator();
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/run.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_CREATEARCHIVES);
        button.addActionListener(this);
        toolbar.add(button);
        /* advanced settings button */
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/options.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_ADVANCEDSETTINGS);
        button.addActionListener(this);
        toolbar.add(button);
        /* help index button */
        toolbar.addSeparator();
        button=new JButton();
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/faq.gif")));
        button.setActionCommand(DebigulatorConstants.ACTION_HELPINDEX);
        button.addActionListener(this);
        toolbar.add(button);
        this.getContentPane().add(toolbar,BorderLayout.NORTH);
    }
    
    /* adds the center panel */
    private void addCenterPanel(){
        JPanel centerPanel=new JPanel();
        centerPanel.setLayout(new GridLayout(2,1));
        /* add file selection panel */
        JPanel filePanel=new JPanel();
        filePanel.setLayout(new GridLayout(1,2));
        /* create panel to browse and select files to archive */
        JPanel leftPanel=new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Select Files to Archive"));
        /* add a filechooser to browse for files */
        this.fileBrowser=new NoFrillsJFileChooser();
        this.fileBrowser.setPreferredSize(new Dimension(DebigulatorConstants.FILE_BROWSER_WIDTH,DebigulatorConstants.FILE_BROWSER_HEIGHT));
        /* add file filters */
        this.fileBrowser.addChoosableFileFilter(new NonArchivedFileFilter());
        this.fileBrowser.addChoosableFileFilter(new SourceCodeFileFilter());
        this.fileBrowser.addChoosableFileFilter(new MicrosoftOfficeFilter());
        this.fileBrowser.addChoosableFileFilter(new GameRomzFilter());
        this.fileBrowser.addChoosableFileFilter(new AudioFileFilter());
        this.fileBrowser.addChoosableFileFilter(new VideoFileFilter());
        /* accept all files by default */
        this.fileBrowser.setAcceptAllFileFilterUsed(true);
        this.fileBrowser.setMultiSelectionEnabled(true);
        /* capture property change events, needed to sync outputDirectoryBrowser */
        this.fileBrowser.addPropertyChangeListener(new PropertyChangeListener(){
            @SuppressWarnings({"unqualified-field-access","synthetic-access"})
			public void propertyChange(PropertyChangeEvent event){
                if(event.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)){
                    /* test if sync is true */
                    if(getSyncToSourceDir()){
                        outputDirectoryBrowser.setCurrentDirectory(fileBrowser.getCurrentDirectory());
                    }
                }
            }
        });        
        /* add fileBrowser to leftPanel */
        leftPanel.add(this.fileBrowser);
        /* panel to hold buttons below the file chooser */
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        JButton button=new JButton(DebigulatorConstants.ACTION_ADDSELECTED);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/plus.gif")));
        button.setMnemonic(KeyEvent.VK_A);
        button.setActionCommand(DebigulatorConstants.ACTION_ADDSELECTED);
        button.addActionListener(this);
        buttonPanel.add(button);
        button=new JButton(DebigulatorConstants.ACTION_ADDALL);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/plus.gif")));
        button.setMnemonic(KeyEvent.VK_L);
        button.setActionCommand(DebigulatorConstants.ACTION_ADDALL);
        button.addActionListener(this);
        buttonPanel.add(button);
        leftPanel.add(buttonPanel,BorderLayout.SOUTH);
        filePanel.add(leftPanel);
        /* create panel to view files that are selected */
        JPanel rightPanel=new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("List of Files to Archive"));
        /* add list to hold selected files */
        this.fileList=new JList(new DefaultListModel());
        JScrollPane scrollPane=new JScrollPane(this.fileList);
        rightPanel.add(scrollPane);
        /* panel to hold buttons below the list */
        buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        button=new JButton(DebigulatorConstants.ACTION_REMOVESELECTED);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/minus.gif")));
        button.setMnemonic(KeyEvent.VK_R);
        button.setActionCommand(DebigulatorConstants.ACTION_REMOVESELECTED);
        button.addActionListener(this);
        buttonPanel.add(button);
        button=new JButton(DebigulatorConstants.ACTION_REMOVEALL);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/minus.gif")));
        button.setMnemonic(KeyEvent.VK_M);
        button.setActionCommand(DebigulatorConstants.ACTION_REMOVEALL);
        button.addActionListener(this);
        buttonPanel.add(button);
        rightPanel.add(buttonPanel,BorderLayout.SOUTH);
        filePanel.add(rightPanel);
        centerPanel.add(filePanel);
        /* add archive settings panel */
        JPanel settingsPanel=new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.Y_AXIS));
        settingsPanel.setAlignmentY(Component.LEFT_ALIGNMENT);
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Archive Settings"));
        //settingsPanel.setLayout(new GridLayout(4,2));
        JPanel tempPanel=new JPanel();
        tempPanel.setLayout(new GridLayout(1,2));
        JLabel label=new JLabel("Output Directory:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempPanel.add(label);
        /* add sync to source directory checkbox */
        this.syncToSourceDirCheckBox=new JCheckBox("Sync to source directory");
        this.syncToSourceDirCheckBox.setMnemonic(KeyEvent.VK_Y);
        this.syncToSourceDirCheckBox.setActionCommand("syncToSourceDirCheckBox");
        this.syncToSourceDirCheckBox.addActionListener(this);
        tempPanel.add(this.syncToSourceDirCheckBox);
        settingsPanel.add(tempPanel);
        /* add directory browser */
        this.outputDirectoryBrowser=new NoFrillsJFileChooser();
        this.outputDirectoryBrowser.setPreferredSize(new Dimension(DebigulatorConstants.OUTPUT_DIRECTORY_BROWSER_WIDTH,DebigulatorConstants.OUTPUT_DIRECTORY_BROWSER_HEIGHT));
        this.outputDirectoryBrowser.setAcceptAllFileFilterUsed(false);
        this.outputDirectoryBrowser.setMultiSelectionEnabled(false);
        /* only show directories - set filter instead of using JFileChooser.DIRECTORIES_ONLY so that text appears in the drop-down */
        this.outputDirectoryBrowser.setFileFilter(new DirectoryFileFilter());
        /* capture property change events, needed to sync with fileBrowser */
        this.outputDirectoryBrowser.addPropertyChangeListener(new PropertyChangeListener(){
            @SuppressWarnings({"unqualified-field-access","synthetic-access"})
			public void propertyChange(PropertyChangeEvent event){
                if(event.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)){
                    /* test if sync is true */
                    if(getSyncToSourceDir()){
                        outputDirectoryBrowser.setCurrentDirectory(fileBrowser.getCurrentDirectory());
                    }
                }
            }
        });   
        settingsPanel.add(this.outputDirectoryBrowser);
        /* add archive type combo box */
        tempPanel=new JPanel();
        tempPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
        label=new JLabel("Archive Type:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempPanel.add(label);
        //this.archiveTypeComboBox=new JComboBox(BatchCompressionThread.getSupportedArchiveTypes());
        this.archiveTypeComboBox=new JComboBox(BatchCompressionThreadParameters.ArchiveTypes.values());
        tempPanel.add(this.archiveTypeComboBox);
        /* add action combo box */
        label=new JLabel("Action After Archive:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempPanel.add(label);
        //this.actionComboBox=new JComboBox(BatchCompressionThread.getSupportedActions());
        this.actionComboBox=new JComboBox(BatchCompressionThreadParameters.ArchiveCompleteActions.values());
        tempPanel.add(this.actionComboBox);
        settingsPanel.add(tempPanel);
        /* add panel to hold buttons */
        settingsPanel.add(new JPanel()); //cheap way to add a little spacing
        buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        button=new JButton(DebigulatorConstants.ACTION_ADVANCEDSETTINGS);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/options.gif")));
        button.setMnemonic(KeyEvent.VK_D);
        button.setActionCommand(DebigulatorConstants.ACTION_ADVANCEDSETTINGS);
        button.addActionListener(this);
        buttonPanel.add(button);
        button=new JButton(DebigulatorConstants.ACTION_CREATEARCHIVES);
        button.setIcon(new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/run.gif")));
        button.setMnemonic(KeyEvent.VK_C);
        button.setActionCommand(DebigulatorConstants.ACTION_CREATEARCHIVES);
        button.addActionListener(this);
        buttonPanel.add(button);
        settingsPanel.add(buttonPanel);
        centerPanel.add(settingsPanel);
        this.getContentPane().add(centerPanel,BorderLayout.CENTER);
    }
    
    /* adds progress bar */
    private void addProgressBar(){
        this.progressBar=new JProgressBar();
        this.progressBar.setStringPainted(true); //why, why is this not default behavior?
        this.progressBar.setAlignmentX(Component.LEFT_ALIGNMENT); //this doesn't appear to do anything
        this.progressBar.setString("Ready");
        this.getContentPane().add(this.progressBar,BorderLayout.SOUTH);
    }
    
    /** 
     * Updates the status text in the progress bar.
     * @param status The new status message to display.
     */
    public void updateStatus(String status){
        this.progressBar.setString(status);
    }
    
    /** 
     * Listener for action events.
     * @param actionEvent The action performed.
     */
    public void actionPerformed(ActionEvent actionEvent){
        String actionCommand=actionEvent.getActionCommand();
        /* handle add & remove functions here */
        if(actionCommand.equals(DebigulatorConstants.ACTION_ADDSELECTED)){
            /* add selected files from fileBrowser to fileList */
            File[] fileList=this.fileBrowser.getSelectedFiles();
            int fileCount=fileList.length;
            for(int fileIndex=0;fileIndex<fileCount;fileIndex++){
                ((DefaultListModel)this.fileList.getModel()).addElement(fileList[fileIndex].getPath());
            }
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_ADDALL)){
            /* add all files from fileBrowser to fileList */
            File[] fileList=this.fileBrowser.getCurrentDirectory().listFiles();
            int fileCount=fileList.length;
            for(int fileIndex=0;fileIndex<fileCount;fileIndex++){
                if(fileList[fileIndex].isFile()){
                    if(this.fileBrowser.getFileFilter().accept(fileList[fileIndex])){
                        ((DefaultListModel)this.fileList.getModel()).addElement(fileList[fileIndex].getPath());
                    }
                }
            }
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_REMOVEALL)){
            /* remove all files from fileList */
            this.fileList.setModel(new DefaultListModel());
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_REMOVESELECTED)){
            /* remove selected files from fileList */
            Object[] selectedValues=this.fileList.getSelectedValues();
            int length=selectedValues.length;
            for(int index=0;index<length;index++){
                ((DefaultListModel)this.fileList.getModel()).removeElement(selectedValues[index]);
            }
//TODO - if file browse panels are correctly implemented turn this feature back on            
//        } else if(actionCommand.equals(DebigulatorConstants.ACTION_CHANGESTYLE)){
//            try{
//                String lookAndFeelClassName=((JMenuItem)actionEvent.getSource()).getText();
//                UIManager.setLookAndFeel(lookAndFeelClassName);
//                SwingUtilities.updateComponentTreeUI(this);
//            } catch(Exception x){
//                x.printStackTrace();
//            }
        } else if(actionCommand.equals("syncToSourceDirCheckBox")){
            this.setSyncToSourceDir(this.syncToSourceDirCheckBox.isSelected());
        } else{ /* send all other action commands to the parent (implementing) class */
            this.sendAction(actionCommand);
        }
    }
    
    /** 
     * Returns the directory selected in the output directory file chooser.
     * @return The path to the output directory.
     */
    public String getOutputDirectory(){
        return(this.outputDirectoryBrowser.getCurrentDirectory().toString());
    }
    
    /** 
     * Sets the directory in the output directory file chooser.
     * @param outputDirectory The full path to the new output directory.
     */
    public void setOutputDirectory(String outputDirectory){
        this.outputDirectoryBrowser.setCurrentDirectory(new File(outputDirectory));
    }

    /** 
     * Returns the directory selected in the file browser file chooser.
     * @return Path to the current directory.
     */
    public String getBrowseDirectory(){
        return(this.fileBrowser.getCurrentDirectory().toString());
    }
    
    /** 
     * Sets the directory in the file browser file chooser.
     * @param browseDirectory The full path to the new directory.
     */
    public void setBrowseDirectory(String browseDirectory){
        this.fileBrowser.setCurrentDirectory(new File(browseDirectory));
    }
    
    /** 
     * Returns the list of files in the file list.
     * @return The list of files in the file list box.
     */
    public String[] getFileList(){
        int size=((DefaultListModel)this.fileList.getModel()).getSize();
        String[] theList=new String[size];
        for(int index=0;index<size;index++){
            theList[index]=(String)(((DefaultListModel)this.fileList.getModel()).get(index));
        }
        return(theList);
    }
    
    /** 
     * Sets the list of files in the file list.
     * @param fileList The list of files to populate the file list with.
     */
    public void setFileList(String[] fileList){
        this.clearFileList();
        int fileCount=fileList.length;
        for(int fileIndex=0;fileIndex<fileCount;fileIndex++){
            ((DefaultListModel)this.fileList.getModel()).addElement(fileList[fileIndex]/*.getPath()*/);
        }
    }
    
    /** 
     * Clears the list of files in the file list.
     */
    public void clearFileList(){
        this.fileList.setModel(new DefaultListModel());
    }
    
    /** 
     * Sets the values (indicies) of the settings combo boxes.
     * @param archiveTypeValue The value (index) for archive type combo box.
     * @param actionValue value The value (index) for action combo box.
     */
    public void setComboBoxValues(int archiveTypeValue,int actionValue){
        this.archiveTypeComboBox.setSelectedIndex(archiveTypeValue);
        this.actionComboBox.setSelectedIndex(actionValue);
    }

    /** 
     * Sets the values of the settings combo boxes.
     * @param archiveType The value for archive type combo box.
     * @param action The value for action combo box.
     */
    public void setComboBoxValues(BatchCompressionThreadParameters.ArchiveTypes archiveType,BatchCompressionThreadParameters.ArchiveCompleteActions action){
    	int archiveTypeIndex=-1;
    	int actionIndex=-1;
    	int index=0;
    	int count=BatchCompressionThreadParameters.ArchiveTypes.values().length;
    	while((index<count)&&(archiveTypeIndex<0)){
    		if(BatchCompressionThreadParameters.ArchiveTypes.values()[index]==archiveType){
    			archiveTypeIndex=index;
    		} else{
        		index++;
    		}
    	}
    	index=0;
    	count=BatchCompressionThreadParameters.ArchiveCompleteActions.values().length;
    	while((index<count)&&(actionIndex<0)){
    		if(BatchCompressionThreadParameters.ArchiveCompleteActions.values()[index]==action){
    			actionIndex=index;
    		} else{
        		index++;
    		}
    	}
    	this.setComboBoxValues(archiveTypeIndex,actionIndex);
    }
    
    /** 
     * Returns the index of the selected item on the archive type combo box.
     * @return The index of the selected item on the archive type combo box.
     */
    public BatchCompressionThreadParameters.ArchiveTypes getSelectedArchiveType(){
        return((BatchCompressionThreadParameters.ArchiveTypes)this.archiveTypeComboBox.getSelectedItem());
    }

    /** 
     * Returns the index of the selected item on the action combo box.
     * @return The index of the selected item on the action combo box.
     */
    public BatchCompressionThreadParameters.ArchiveCompleteActions getSelectedAction(){
        return((BatchCompressionThreadParameters.ArchiveCompleteActions)this.actionComboBox.getSelectedItem());
    }
    
    /** 
     * Sets whether or not the sync to source directory check box is checked.
     * @param checked Set to true to check the box, false to uncheck it.
     */
    protected void setSyncToSourceDir(boolean checked){
        this.syncToSourceDirCheckBox.setSelected(checked);
        /* if this property is true, need to disable outputDirectoryBrowser and sync it to fileBrowser */
        if(checked){
            this.outputDirectoryBrowser.setCurrentDirectory(this.fileBrowser.getCurrentDirectory());
        } 
    }
    
    /** 
     * Returns whether or not the sync to source directory check box is checked.
     * @return True if box is checked, false if box is unchecked.
     */
    protected boolean getSyncToSourceDir(){
        return(this.syncToSourceDirCheckBox.isSelected());
    }    
    
    /** 
     * Forces the file lists to refresh; after an archive operation, the files listed in fileBrowser and fileList may have been moved or deleted.
     */
    public void refreshFiles(){
       /* refresh fileBrowser */
        this.fileBrowser.rescanCurrentDirectory();
        /* refresh fileList */
        int size=((DefaultListModel)this.fileList.getModel()).getSize();
        int index=0;
        while(index<size){
            /* if the file doesn't exist any more, remove it from fileList */
            if(!new File((String)(((DefaultListModel)this.fileList.getModel()).get(index))).exists()){
                ((DefaultListModel)this.fileList.getModel()).remove(index);
                /* removing this element shifts everything down one, leave index the same and decrement size */
                size--;
            } else{
                /* move to the next element */
                index++;
            }
        }
    }

    /** 
     * Sends an action back to the parent class.
     * Override this function to receive GUI events.
     * @param actionCommand the command executed,
     */
    public abstract void sendAction(String actionCommand);
}
