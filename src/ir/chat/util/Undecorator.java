package ir.chat.util;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Hamed on 9/10/2015.
 */
public class Undecorator{
    private Stage stage;
    private double border;
    private Region moveArea;
    private boolean isResizable = true;
    private boolean isMovable = false;

    public static final Undecorator UndecoratorFactory(Stage stage, double border) {
        return UndecoratorFactory(stage, border, null, true);
    }

    public static final Undecorator UndecoratorFactory(Stage stage, double border, Region moveArea) {
        return UndecoratorFactory(stage, border, moveArea, true);
    }

    public static final Undecorator UndecoratorFactory(Stage stage, double border, Region moveArea, boolean isResizable) {
        stage.initStyle(StageStyle.UNDECORATED);
        Undecorator wrapper = new Undecorator(stage,border);

        Parent root = stage.getScene().getRoot();
        AnchorPane wrapperPane = new AnchorPane();
        wrapperPane.setStyle("-fx-background-color: rgba(128,128,128,0.01);");
        wrapperPane.getChildren().add(root);
        AnchorPane.setTopAnchor(root, border);
        AnchorPane.setRightAnchor(root, border);
        AnchorPane.setBottomAnchor(root, border);
        AnchorPane.setLeftAnchor(root, border);

        wrapper.stage = stage;
        wrapper.border = border;
        if(moveArea != null) {
            wrapper.moveArea = moveArea;
            wrapper.setIsMovable(true);
        }
        wrapper.setIsResizable(isResizable);
        wrapper.addResizeListener(stage);

        stage.getScene().setRoot(wrapperPane);
        return wrapper;
    }

    protected Undecorator(Stage stage, double border) {
        this.stage = stage;
        this.border = border;
    }

    public double getBorderSize() {
        return border;
    }

    public void setBorderSize(double border) {
        this.border = border;
    }

    public Region getMoveArea() {
        return moveArea;
    }

    public void setMoveArea(Region moveArea) {
        this.moveArea = moveArea;
    }

    public boolean getisResizable() {
        return isResizable;
    }

    public void setIsResizable(boolean isResizable) {
        this.isResizable = isResizable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    protected void addResizeListener(Stage stage) {
        if(isMovable() || getisResizable()) {
            MouseListener mouseListener = new MouseListener(stage, moveArea);
            stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, mouseListener);
            stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseListener);
            stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseListener);
            ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, mouseListener);
            }
        }
    }

    protected void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    class MouseListener implements EventHandler<MouseEvent> {
        private Stage stage;
        Region moveArea;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private double startX = 0;
        private double startY = 0;
        private double distanceX = 0;
        private double distanceY = 0;

        public MouseListener(Stage stage) {
            this.stage = stage;
        }

        public MouseListener(Stage stage, Region moveArea) {
            this.stage = stage;
            this.moveArea = moveArea;
        }

        public void setMoveArea(Region moveArea) {
            this.moveArea = moveArea;
        }

        public Region getMoveArea() {
            return moveArea;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends Event> mouseEventType = mouseEvent.getEventType();
            Scene scene = stage.getScene();

            double mouseEventX = mouseEvent.getSceneX(), //current mouse's x position
                    mouseEventY = mouseEvent.getSceneY(), //current mouse's y position
                    sceneWidth = scene.getWidth(), //current scene width
                    sceneHeight = scene.getHeight(); //current scene height

//            System.out.println("******************************************************************\r\n" +
//                    "* mouseEventX: "+mouseEventX+"                                   *\n" +
//                    "* mouseEventY: "+mouseEventY+"                                   *\n" +
//                    "* sceneWidth: "+sceneWidth+"                                     *\n" +
//                    "* sceneHeight: "+sceneHeight+"                                   *\n" +
//                               "******************************************************************\n");
            if (!stage.isMaximized()) {
                if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) { //Set the Cursor at edges
                    if(getisResizable()) {
                        if (mouseEventX < border && mouseEventY < border) {
                            cursorEvent = Cursor.NW_RESIZE;
                        } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                            cursorEvent = Cursor.SW_RESIZE;
                        } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                            cursorEvent = Cursor.NE_RESIZE;
                        } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                            cursorEvent = Cursor.SE_RESIZE;
                        } else if (mouseEventX < border) {
                            cursorEvent = Cursor.W_RESIZE;
                        } else if (mouseEventX > sceneWidth - border) {
                            cursorEvent = Cursor.E_RESIZE;
                        } else if (mouseEventY < border) {
                            cursorEvent = Cursor.N_RESIZE;
                        } else if (mouseEventY > sceneHeight - border) {
                            cursorEvent = Cursor.S_RESIZE;
                        } else {
                            cursorEvent = Cursor.DEFAULT;
                        }
                        scene.setCursor(cursorEvent);
                    }
                } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
                    //store distance between mouse and stage's edge
                    distanceX = stage.getWidth() - mouseEventX;
                    distanceY = stage.getHeight() - mouseEventY;
                    startX = mouseEventX;
                    startY = mouseEventY;
                } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) { //Resize or Move the Stage

                    if (!Cursor.DEFAULT.equals(cursorEvent)) { //if the mouse is at edges
                        if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) { //if cursor is not at sides (at left or right)
                            double minHeight = stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2); //Calculate the minimum Height
                            if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) { //if Cursor is at top edge or one of top corners
                                if (stage.getHeight() > minHeight || mouseEventY < 0) {
                                    stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
//                                System.out.println("stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight()); = stage.setHeight("+stage.getY()+" - "+mouseEvent.getScreenY()+" + "+stage.getHeight()+"); = stage.setHeight("+(stage.getY() - mouseEvent.getScreenY() + stage.getHeight())+");");
                                    stage.setY(mouseEvent.getScreenY());
                                }
                            } else { //if Cursor is at bottom edge or one of bottom corners
                                if (stage.getHeight() > minHeight || mouseEventY + distanceY - stage.getHeight() > 0) {
                                    stage.setHeight(mouseEventY + distanceY);
//                                System.out.println("stage.setHeight(mouseEventY + distanceY); = stage.setHeight("+mouseEventY+" + "+distanceY+");");
                                }
                            }
                        }

                        if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) { //if cursor is at sides (at left or right)
                            double minWidth = stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
                            if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) { //if Cursor is at left edge or one of left corners
                                if (stage.getWidth() > minWidth || mouseEventX < 0) {
                                    stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                                    stage.setX(mouseEvent.getScreenX());
                                }
                            } else { //if Cursor is at right edge or one of right corners
                                if (stage.getWidth() > minWidth || mouseEventX + distanceX - stage.getWidth() > 0) {
                                    stage.setWidth(mouseEventX + distanceX);
                                }
                            }
                        }
                    } else // moving behavior
                    {
                        if (isMovable()) {
                            if (ifMouseWithinMoveArea(startX, startY) && mouseEvent.getSource().equals(moveArea)) {
                                stage.setX(mouseEvent.getScreenX() - startX);
                                stage.setY(mouseEvent.getScreenY() - startY);
//                            System.out.println("moveArea.getLayoutX()" + moveArea.getLayoutX());
//                            System.out.println("moveArea.getLayoutY()" + moveArea.getLayoutY());
                            }
                        }
                    }
                }
            }
        }

        private boolean ifMouseWithinMoveArea(double startX, double startY) {
            if (startX > moveArea.getLayoutX() && startX < moveArea.getLayoutX() + moveArea.getWidth() &&
                    startY > moveArea.getLayoutY() && startY < moveArea.getLayoutY() + moveArea.getHeight())
                return true;
            return false;
        }
    }
}
