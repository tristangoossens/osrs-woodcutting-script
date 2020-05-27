public enum Wood {
    NORMAL("Tree", "Logs"), OAK("Oak", "Oak logs"), WILLOW("Willow", "Willow logs"), MAPLE("Maple tree", "Maple logs"), YEW("Yew tree", "Yew logs");

    private String logName;
    private String treeName;

    Wood(String treeName, String logName){
        setLogName(logName);
        setTreeName(treeName);
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
}
