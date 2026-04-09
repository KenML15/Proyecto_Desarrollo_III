/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Kenneth
 */
public class Space {

    private int id;
    private boolean disabilityAdaptation;
    private boolean spaceTaken;

    public Space(int id, boolean disabilityAdaptation, boolean spaceTaken) {
        this.id = id;
        this.disabilityAdaptation = disabilityAdaptation;
        this.spaceTaken = spaceTaken;
    }

    public Space() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDisabilityAdaptation() {
        return disabilityAdaptation;
    }

    public void setDisabilityAdaptation(boolean disabilityAdaptation) {
        this.disabilityAdaptation = disabilityAdaptation;
    }

    public boolean isSpaceTaken() {
        return spaceTaken;
    }

    public void setSpaceTaken(boolean spaceTaken) {
        this.spaceTaken = spaceTaken;
    }
    
    

}
