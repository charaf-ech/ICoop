package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public final class ICoopBehavior extends AreaBehavior {
    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }

    public enum ICoopCellType {
        NULL(0, false , false),
        WALL(-16777216, false , false),
        IMPASSABLE (-8750470, false , true),
        INTERACT(-256, true , true),
        DOOR(-195580, true , true),
        WALKABLE(-1, true , true),
        ROCK(-16777204, true , true),
        OBSTACLE (-16723187, true , true)
        ;

        final int type;
        final boolean canWalk;
        final boolean canFly;

        ICoopCellType(int type, boolean canWalk,boolean canFly ) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            return NULL;
        }
    }

    public class ICoopCell extends Cell {
        /// Type of the cell following the enum
        private final ICoopCellType type;

        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if(!type.canWalk) return false;
            for ( Interactable i : entities) {
                if (i.takeCellSpace()){
                    return false;
                }
            }
            return true;
        }



        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            if (v instanceof ICoopInteractionVisitor visitor) {
                visitor.interactWith(this, isCellInteraction);
            }
        }

    }
}
