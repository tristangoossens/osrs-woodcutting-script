public enum Wood {
	// ENUM containing all tree types in the game, that are available to F2P players.
    NORMAL("Tree", "Logs"), OAK("Oak", "Oak logs"), WILLOW("Willow", "Willow logs"), MAPLE("Maple tree", "Maple logs"), YEW("Yew tree", "Yew logs");

	// Each entry in the enum will contain both the logname and treename.
    private String logName;
    private String treeName;

    // Set the string values using the set methods
    Wood(String treeName, String logName){
        setLogName(logName);
        setTreeName(treeName);
    }

    
    // Standard get/set functions for the logname.
    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    // Standard get/set functions for the treename.
    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
}
