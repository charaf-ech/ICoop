package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public final class ICoopBehavior extends AreaBehavior {
    public ICoopBehavior(Window window , String name){
        super(window,name);
        int height = getHeight();
        int width = getWidth();
        for ( int y=0 ; y<height ; y++){
            for (int x=0 ; x<width ; x++){
                ICoopCellType color = ICoopCellType.toType(getRGB(-1-y,x));
                setCell(x,y,new ICoopCell(x,y,color));
            }
        }

    }
    public enum ICoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0, false),
        WALL(-16777216, false),
        IMPASSABLE(-8750470, false),
        INTERACT(-256, true),
        DOOR(-195580, true),
        WALKABLE(-1, true),
        ;

        final int type;
        final boolean isWalkable;

        ICoopCellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ICoopCellType toType( int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            System.out.println(type);
            return NULL;
        }
    }
    public class ICoopCell extends Cell {
        private final ICoopCellType type;

        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
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
            return type.isWalkable;
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
        }

    }
}
