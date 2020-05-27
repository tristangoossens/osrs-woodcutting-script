import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(category = Category.WOODCUTTING, name = "Woodcutting script", author = "nl3choppa", description = "woodcutting bot by tristangoossens, informational purposes only", version = 1)
public class Main extends AbstractScript {

    // 3 types of states
    //
    // 0: Ready to chop
    // 1: Inventory full
    // 2: Dropping items
    private String state;
    private Wood currentLog;
    private boolean drop = false;

    // onStart is a function overwritten from the
    @Override
    public void onStart() {
        super.onStart();
        state = "chopping_wood";
        currentLog = Wood.OAK;
        log("Lets go");
    }

    @Override
    public int onLoop() {
        if (state == "chopping_wood") {
            if (!getInventory().isFull()) {
                GameObject gO = getGameObjects().closest(f -> f.getName().equals(currentLog.getTreeName()));
                if (getLocalPlayer().distance(gO) > 5) {
                    getWalking().walk(gO);
                    sleepUntil(() -> getLocalPlayer().isMoving()
                            || getLocalPlayer().distance(getClient().getDestination()) < 7, Calculations.random(4400, 5200));
                } else {
                    if (gO.interact("Chop down")) {
                        sleepUntil(() -> !gO.exists() || !getLocalPlayer().isAnimating(), Calculations.random(12000, 15000));
                    }
                }
            } else {
                if (drop) {
                    state = "dropping_items";
                } else {
                    state = "banking_items";
                }
            }
        } else if (state == "banking_items") {
           bank();
        } else if (state == "dropping_items") {
            if (getInventory().contains(currentLog.getLogName())) {
                getInventory().dropAll(currentLog.getLogName());
            } else {
                state = "chopping_wood";
            }
        }
        return Calculations.random(300, 400);
    }
    
    private void bank() {
    	 if (getBank().isOpen()) {
             getBank().depositAllExcept(f -> f.getName().contains("Bronze axe"));
             getBank().close();
             sleepUntil(() -> !getBank().isOpen(), Calculations.random(2000, 2800));
             state = "chopping_wood";
         } else {
             if (getLocalPlayer().distance(getBank().getClosestBankLocation().getCenter()) > 5) {
                 if (getWalking().walk(getBank().getClosestBankLocation().getCenter())) {
                     sleepUntil(() -> !getLocalPlayer().isMoving()
                             || getLocalPlayer().distance(getClient().getDestination()) < 8, Calculations.random(3500, 5000));
                 } 
             }  
             else {
                     getBank().open();
                     sleepUntil(() -> getBank().isOpen(), Calculations.random(2000, 2800));
                     return;
             }           
         }
    }
}
