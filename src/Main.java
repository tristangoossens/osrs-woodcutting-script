import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

// This script is written by Tristan Goossens for a Java school project. Use the script at your own risk as scripting in OSRS is bannable.

@ScriptManifest(category = Category.WOODCUTTING, name = "Woodcutting script", author = "tristangoossens", description = "woodcutting script for educational purposes", version = 1.0)
public class Main extends AbstractScript {

    private String state;
    private Wood currentLog;
    private boolean drop;

    // onStart is a function from the DreamBot client that is called when the game starts
    @Override
    public void onStart() {
        super.onStart();
        // Set state to chopping
        state = "chopping_wood";
        // Choose wood type
        currentLog = Wood.NORMAL;
        // Log start
        log("Starting");
    }

    // onLoop is a function from the DreamBot client that is called on loop during the duration of the script running
    @Override
    public int onLoop() {
    	// Switch the state on loop. So that the script can handle accordingly
    	if (state == "chopping_wood") {
    		chopping();
        } else if (state == "banking_items") {
            banking();
        } else if (state == "dropping_items") {
            dropping();
        }
        // Wait a random amount of milliseconds between 300 and 400 then loop again
        return Calculations.random(300, 400);
    }
    
    
    // chopping is a function that is called when the state is set to chopping, it will keep chopping until the inventory is full
    private void chopping() {
    	// Check if the inventory is not full
    	if (!getInventory().isFull()) {
    		// Create a new game object containing the location of the closest tree
            GameObject gO = getGameObjects().closest(f -> f.getName().equals(currentLog.getTreeName()));
            // Check if the player is more then 5 steps away from the closest tree.
            if (getLocalPlayer().distance(gO) > 5) {
            	// Click on where the player is going
                getWalking().walk(gO);
                // Sleep until the player is not moving anymore, or click again when the player is within 7 spaces.
                sleepUntil(() -> getLocalPlayer().isMoving()
                        || getLocalPlayer().distance(getClient().getDestination()) < 7, Calculations.random(4400, 5200));
            } else {
            	// Check if the player is chopping the tree
                if (gO.interact("Chop down")) {
                	// Sleep until the tree is gone or the player stops animating moves
                    sleepUntil(() -> !gO.exists() || !getLocalPlayer().isAnimating(), Calculations.random(12000, 15000));
                }
            }
        // If inventory is full check if drop is enabled, if not it will bank. Else it will drop all items
        } else {
            if (drop) {
                state = "dropping_items";
            } else {
                state = "banking_items";
            }
        }
    }
    
    // Banking is a function that is called when the state is set to banking, the state will be changed to chopping when the script has banked
    private void banking() {
    	 // Check if the bank is open
    	 if (getBank().isOpen()) {
    		 // Deposit everything except the axe, then close the bank
             getBank().depositAllExcept(f -> f.getName().contains("Bronze axe"));
             getBank().close();
             // Sleep until the bank is closed
             sleepUntil(() -> !getBank().isOpen(), Calculations.random(2000, 2800));
             // Set state to chopping
             state = "chopping_wood";
         // If bank is not open the player will move to the bank
         } else {
        	// Check if the player is more then 5 steps away from the closest bank.
             if (getLocalPlayer().distance(getBank().getClosestBankLocation().getCenter()) > 5) {
            	 // Walk to the center of the bank.
                 if (getWalking().walk(getBank().getClosestBankLocation().getCenter())) {
                	// Sleep until the player is not moving anymore, or click again when the player is within 8 spaces.
                     sleepUntil(() -> !getLocalPlayer().isMoving()
                             || getLocalPlayer().distance(getClient().getDestination()) < 8, Calculations.random(3500, 5000));
                 } 
             }  
             // If the player is close to a bank interact with it
             else {
                     getBank().open();
                     // Sleep until the bank is open, then return to the top of the function.
                     sleepUntil(() -> getBank().isOpen(), Calculations.random(2000, 2800));
                     return;
             }           
         }
    }
    
    // Dropping is a function that is called when the state is set to dropping, the state will be changed to chopping when the script has dropped all logs
    private void dropping() {
    	if (getInventory().contains(currentLog.getLogName())) {
            getInventory().dropAll(currentLog.getLogName());
        } else {
            state = "chopping_wood";
        }
    }
}
