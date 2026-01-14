package com.rivves.game.world.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rivves.game.Handler;
import com.rivves.game.Utilities.BeltChangeEvent;
import com.rivves.game.Utilities.Point3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ConveyorBeltManager {

    //this class handles all of the conveyorBelts and their segments, and the world has one of these

    private Handler handler;

    private ArrayList<ConveyorBeltSegment> segments;

    private ArrayList<BeltChangeEvent> changes; //3 integers in the point, x , y, rotation,
    //any changes made to the belts in the world, if we delete one, then
    //x, y, 0

    private boolean updateConveyorSegments = false;


    public ConveyorBeltManager(Handler handler){
        this.handler = handler;
        segments = new ArrayList<>();
        changes = new ArrayList<>();
    }

    public void tick(){
        for(ConveyorBeltSegment segment : segments){
            segment.tick();
        }

        if(updateConveyorSegments)
            UpdateSegments();

    }
    public void renderDebugBatch(SpriteBatch sb){
        for(ConveyorBeltSegment segment : segments){
            segment.renderDebugBatch(sb);
        }
    }
    public void renderDebug(ShapeRenderer sr){
        for(ConveyorBeltSegment segment : segments){

            segment.renderDebug(sr);
            //now we want to check if we are hovering over the segment if its hovered
            Point mouseBlockCoords = handler.getStateManager().getGameState().getMouseBlockCoords();
            if(getSegment(mouseBlockCoords.x, mouseBlockCoords.y) != null)
                getSegment(mouseBlockCoords.x, mouseBlockCoords.y).renderHover(sr);
        }
    }

    private void UpdateSegments(){

        for(BeltChangeEvent beltChangeEvent : changes){
            //loop through every point and make the appropriate changes to the conveyor segments

            int x = beltChangeEvent.x;
            int y = beltChangeEvent.y;
            int type = beltChangeEvent.type;        //if delete that the type is the type of belt that was deleted
            boolean isDelete = beltChangeEvent.isDelete;

            int typeLeft = handler.getCurrentWorld().getBelts()[x - 1][y];
            int typeRight = handler.getCurrentWorld().getBelts()[x + 1][y];
            int typeUp = handler.getCurrentWorld().getBelts()[x][y + 1];
            int typeDown = handler.getCurrentWorld().getBelts()[x][y - 1];



            //maybe if we are trying to make a change to something already their that just fuck that event?


            //okay so in the case of index 2, now
            //s t t t t e
            //s t t e s t t e
            //t t e s t t
            //SHIFT index + 1?
            //okay so if the arrays are not the same size, its not accurate, it seems that whichever array is longer,
            //the points lean towards

            //why?
            //okay imagine an uneven array
                                /*
                                        0 1 2 3 4 5 6 7 8 9
                                        s t t t t t t t t e   say we cut between 7 and 8

                                        0 1 2 3 4 5 6 7   8 9
                                        s t t t t t t t   t e


                                        0 1 2 3 4 5 6 7 8 9 10 11
                                        s t t t t t t t e s t  e        //add new start and end

                                        0 1 2 3 4 5 6 7 8 9
                                        t t t t t t t e s t             //cut off start and end
                                                                    remember indexToCut == 7
                                                                    lets shift segment.sixe - (indexToCut + 1)


                                        NEW EXAMPLE
                                        0 1 2 3 4 5
                                        s t t t t e

                                        0 1 2   3 4 5
                                        s t t   t t e

                                        0 1 2 3 4 5 6 7
                                        s t t e s t t e

                                        0 1 2 3 4 5
                                        t t e s t t  now we need to shift, indexToCut = 2 + 1 = 3, this works


                                 */


            if(isDelete){
                ConveyorBeltSegment segment = getSegment(x, y);
                int indexToCut = segment.getIndexJustBefore(x, y);
                ArrayList<Point> first = new ArrayList<>(segment.getPoints().subList(0, indexToCut + 1));
                ArrayList<Point> second = new ArrayList<>(segment.getPoints().subList(indexToCut + 1, segment.getNumberOfPoints()));
                System.out.println("First: " + first);
                System.out.println("Second: " + second);

                System.out.println("BELT REMOVED: " + type);
                /*
                            okay so how can I split them, well

                            okay when we remove a rail, there is either 0, 1, or 2 rail neighbors
                            if there is 0, delete the segment and remove the rail
                            if there is 1 shift the start/end
                            if there is 2, determine if they are the same segment(a loop)
                                if so, move start and end around deleted point
                                if not, split into 2 segments, end of first next to me and start of second to other side


                                first determine what type we are removing, then check appropriate places to check for other rail
                 */
                switch (type){
                    case 1: //Right
                        //okay so we are deleting a rail that is facing right
                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){

                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x - 1, y);
                                segment.addPoint(indexToCut + 2, x + 1, y);

                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                System.out.println("IS NOT A LOOP");
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);

                                newLeft.addEnd(x - 1, y);
                                newRight.addStart(x + 1, y);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }

                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){
                                segment.setEndPoint(x - 1, y);
                            }else{
                                segment.setStartPoint(x + 1, y);
                            }
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 2: //Down

                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x, y + 1);//adding end first
                                segment.addPoint(indexToCut + 2, x, y - 1);

                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);

                                newLeft.addEnd(x, y + 1);
                                newRight.addStart(x, y - 1);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }
                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if((typeUp == 2 || typeUp == 5 || typeUp == 7)){
                                segment.setEndPoint(x, y + 1);
                            }else{
                                segment.setStartPoint(x, y - 1);
                            }
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 3: //Left

                        if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8) && (typeRight == 3 || typeRight == 10 || typeRight == 12)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x + 1, y);
                                segment.addPoint(indexToCut + 2, x - 1, y);

                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(second);

                                newRight.addEnd(x + 1, y);
                                newLeft.addStart(x - 1, y);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }
                        }else if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8) || (typeRight == 3 || typeRight == 10 || typeRight == 12)){
                            if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                                segment.setStartPoint(x - 1, y);
                            }else{
                                segment.setEndPoint(x + 1, y);
                            }
                        }else{
                            segments.remove(segment);
                        }
                        break;
                    case 4: //Up
                        if((typeUp == 4 || typeUp == 11 || typeUp == 12) && (typeDown == 4 || typeDown == 6 || typeDown == 8)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x, y - 1);//adding end first
                                segment.addPoint(indexToCut + 2, x, y + 1);

                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);

                                newLeft.addEnd(x, y - 1);
                                newRight.addStart(x, y + 1);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }
                        }else if((typeUp == 4 || typeUp == 11 || typeUp == 12) || (typeDown == 4 || typeDown == 6 || typeDown == 8)){
                            if((typeUp == 4 || typeUp == 11 || typeUp == 12)){
                                segment.setStartPoint(x, y + 1);
                            }else{
                                segment.setEndPoint(x, y - 1);
                            }
                        }else{
                            segments.remove(segment);
                        }
                        break;
                    case 5: //LeftDown

                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x - 1, y);//adding end first
                                segment.addPoint(indexToCut + 2, x, y - 1);
                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newLeft.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newLeft.addEnd(x - 1, y);
                                newRight.addStart(x, y - 1);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }
                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){
                                segment.setEndPoint(x - 1, y);
                            }else{
                                segment.setStartPoint(x, y - 1);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 6: //LeftUp

                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x - 1, y);//adding end first
                                segment.addPoint(indexToCut + 2, x, y + 1);
                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newLeft = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newLeft.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newLeft.addEnd(x - 1, y);
                                newRight.addStart(x, y + 1);

                                segments.remove(segment);
                                segments.add(newLeft);
                                segments.add(newRight);
                            }
                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){
                                segment.setEndPoint(x - 1, y);
                            }else{
                                segment.setStartPoint(x, y + 1);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 7: //RightDown

                        if((typeRight == 3 || typeRight == 10 || typeRight == 12) && (typeDown == 2 || typeDown == 5 || typeDown == 7)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x + 1, y);//adding end first
                                segment.addPoint(indexToCut + 2, x, y - 1);
                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newDown = new ConveyorBeltSegment(second);
                                newRight.removePoint(x, y);
                                newDown.removePoint(x, y);
                                newRight.addEnd(x + 1, y);
                                newDown.addStart(x, y - 1);

                                segments.remove(segment);
                                segments.add(newRight);
                                segments.add(newDown);
                            }
                        }else if((typeRight == 3 || typeRight == 10 || typeRight == 12) || (typeDown == 2 || typeDown == 5 || typeDown == 7)){
                            if(typeRight == 3 || typeRight == 10 || typeRight == 12){
                                segment.setEndPoint(x + 1, y);
                            }else{
                                segment.setStartPoint(x, y - 1);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 8: //RightUp

                        if((typeRight == 3 || typeRight == 10 || typeRight == 12) && (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            if(isLoop(segment)){
                                segment.addPoint(indexToCut + 1, x + 1, y);//adding end first
                                segment.addPoint(indexToCut + 2, x, y + 1);
                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newDown = new ConveyorBeltSegment(second);
                                newRight.removePoint(x, y);
                                newDown.removePoint(x, y);
                                newRight.addEnd(x + 1, y);
                                newDown.addStart(x, y + 1);

                                segments.remove(segment);
                                segments.add(newRight);
                                segments.add(newDown);
                            }
                        }else if((typeRight == 3 || typeRight == 10 || typeRight == 12) || (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            if(typeRight == 3 || typeRight == 10 || typeRight == 12){
                                segment.setEndPoint(x + 1, y);
                            }else{
                                segment.setStartPoint(x, y + 1);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 9: //UpRight

                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if(isLoop(segment)){

                                segment.addPoint(indexToCut + 1, x, y + 1);//adding end first
                                segment.addPoint(indexToCut + 2, x + 1, y);

                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newUp = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newUp.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newUp.addEnd(x, y + 1);
                                newRight.addStart(x + 1, y);

                                segments.remove(segment);
                                segments.add(newUp);
                                segments.add(newRight);
                            }
                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if(typeUp == 2 || typeUp == 5 || typeUp == 7){
                                segment.setEndPoint(x, y + 1);
                            }else{
                                segment.setStartPoint(x + 1, y);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 10: //UpLeft

                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if(isLoop(segment)){

                                segment.addPoint(indexToCut + 1, x, y + 1);//adding end first
                                segment.addPoint(indexToCut + 2, x - 1, y);

                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newUp = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newUp.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newUp.addEnd(x, y + 1);
                                newRight.addStart(x - 1, y);

                                segments.remove(segment);
                                segments.add(newUp);
                                segments.add(newRight);
                            }
                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if(typeUp == 2 || typeUp == 5 || typeUp == 7){
                                segment.setEndPoint(x, y + 1);
                            }else{
                                segment.setStartPoint(x - 1, y);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 11: //DownRight

                        if((typeDown == 4 || typeDown == 6 || typeDown == 8) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if(isLoop(segment)){

                                segment.addPoint(indexToCut + 1, x, y - 1);//adding end first
                                segment.addPoint(indexToCut + 2, x + 1, y);

                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newBottom = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newBottom.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newBottom.addEnd(x, y - 1);
                                newRight.addStart(x + 1, y);

                                segments.remove(segment);
                                segments.add(newBottom);
                                segments.add(newRight);
                            }
                        }else if((typeDown == 4 || typeDown == 6 || typeDown == 8) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if(typeDown == 4 || typeDown == 6 || typeDown == 8){
                                segment.setEndPoint(x, y - 1);
                            }else{
                                segment.setStartPoint(x + 1, y);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                    case 12: //DownLeft

                        if((typeDown == 4 || typeDown == 6 || typeDown == 8) && (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if(isLoop(segment)){

                                segment.addPoint(indexToCut + 1, x, y - 1);//adding end first
                                segment.addPoint(indexToCut + 2, x - 1, y);

                                segment.removePoint(x, y);//remove the turn we deleted
                                segment.removeStartAndEnd();
                                Collections.rotate(segment.getPoints(), segment.getNumberOfPoints() - (indexToCut + 1));
                            }else{
                                ConveyorBeltSegment newBottom = new ConveyorBeltSegment(first);
                                ConveyorBeltSegment newRight = new ConveyorBeltSegment(second);
                                newBottom.removePoint(x, y);
                                newRight.removePoint(x, y);
                                newBottom.addEnd(x, y - 1);
                                newRight.addStart(x - 1, y);

                                segments.remove(segment);
                                segments.add(newBottom);
                                segments.add(newRight);
                            }
                        }else if((typeDown == 4 || typeDown == 6 || typeDown == 8) || (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if(typeDown == 4 || typeDown == 6 || typeDown == 8){
                                segment.setEndPoint(x, y - 1);
                            }else{
                                segment.setStartPoint(x - 1, y);
                            }
                            segment.removePoint(x, y);
                        }else{
                            segments.remove(segment);
                        }

                        break;
                }




            }else{
                switch (type){
                    case 1://  Right

                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            //then join both sides into 1 segment
                            //how do I join both segments?

                            //okay so joining in the cardinal directions should be rather straight forward

                            //okay so we copy both lists, remove the end from the first and the start of the next, and add them

                            ConveyorBeltSegment leftConveyor = getSegment(x - 1, y);
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);

                            if(leftConveyor == rightConveyor){
                                leftConveyor.setEndPoint(x, y);
                            }else{
                                ArrayList<Point> leftPoints = leftConveyor.getPoints();
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();

                                leftPoints.remove(leftPoints.size() - 1);
                                rightPoints.remove(0);
                                leftPoints.addAll(rightPoints);
                                segments.remove(rightConveyor);
                            }




                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            //then one of the sides is right, join that side
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){
                                //then right is bad side, this segment should join left side
                                //okay so we are facing right, and typeRight == 0
                                //so we want to create a new segment if the left doesnt point into this
                                ConveyorBeltSegment segment = getSegment(x - 1, y);

                                segment.setEndPoint(x , y);

                            }else{
                                //then left side is bad side, this segment should join right side
                                //okay so we placed a rail facing right, and their is a rail to the right, depending on thats rotation, add or create a new segment
                                //okay we want to create a new rail if rightrail doesnt face right, rightdown or rightup
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.setStartPoint(x, y);
                                //also if that point is 5 or 6, then that point needs to become a turn
                            }
                        }else{
                            //otherwise both sides are wrong, create a new segment
                            segments.add(new ConveyorBeltSegment(new Point(x, y)));
                        }

                        break;
                    case 2://  Down
                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);

                            if(topConveyor == bottomConveyor){
                                topConveyor.setEndPoint(x, y);
                            }else{
                                ArrayList<Point> topPoints = topConveyor.getPoints();
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();

                                topPoints.remove(topPoints.size() - 1);
                                bottomPoints.remove(0);
                                topPoints.addAll(bottomPoints);
                                segments.remove(bottomConveyor);
                            }
                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if((typeUp == 2 || typeUp == 5 || typeUp == 7)){
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.setEndPoint(x , y);

                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.setStartPoint(x, y);
                            }
                        }else{
                            segments.add(new ConveyorBeltSegment(new Point(x, y)));
                        }
                        break;
                    case 3://  Left
                        if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8) && (typeRight == 3 || typeRight == 10 || typeRight == 12)){
                            ConveyorBeltSegment leftConveyor = getSegment(x - 1, y);
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);
                            if(leftConveyor == rightConveyor){
                                leftConveyor.setEndPoint(x, y);
                            }else{
                                ArrayList<Point> leftPoints = leftConveyor.getPoints();
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();

                                rightPoints.remove(rightPoints.size() - 1);
                                leftPoints.remove(0);
                                rightPoints.addAll(leftPoints);
                                segments.remove(leftConveyor);
                            }

                        }else if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8) || (typeRight == 3 || typeRight == 10 || typeRight == 12)){
                            if((typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                                ConveyorBeltSegment segment = getSegment(x - 1, y);
                                segment.setStartPoint(x, y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.setEndPoint(x, y);
                            }
                        }else{
                            segments.add(new ConveyorBeltSegment(new Point(x, y)));
                        }
                        break;
                    case 4://  Up
                        if((typeUp == 4 || typeUp == 11 || typeUp == 12) && (typeDown == 4 || typeDown == 6 || typeDown == 8)){
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);

                            if(topConveyor == bottomConveyor){
                                topConveyor.setEndPoint(x, y);
                            }else{
                                ArrayList<Point> topPoints = topConveyor.getPoints();
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();

                                bottomPoints.remove(bottomPoints.size() - 1);
                                topPoints.remove(0);
                                bottomPoints.addAll(topPoints);
                                segments.remove(topConveyor);
                            }

                        }else if((typeUp == 4 || typeUp == 11 || typeUp == 12) || (typeDown == 4 || typeDown == 6 || typeDown == 8)){
                            if((typeUp == 4 || typeUp == 11 || typeUp == 12)){
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.setStartPoint(x , y);

                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.setEndPoint(x, y);
                            }
                        }else{
                            segments.add(new ConveyorBeltSegment(new Point(x, y)));
                        }
                        break;


                    case 5://  LeftDown
                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeDown == 2 || typeDown == 9 | typeDown == 10)){
                            //in the case of both good, connect!
                            ConveyorBeltSegment LeftConveyor = getSegment(x - 1, y);
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);

                            //now we want to see if they are the same segment, cause if they are just move the end point
                            if(LeftConveyor == bottomConveyor){
                                LeftConveyor.setEndPoint(x, y);
                                LeftConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> LeftPoints = LeftConveyor.getPoints();
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();

                                LeftPoints.remove(LeftPoints.size() - 1);   //remove end of first
                                LeftPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                bottomPoints.remove(0);                     //remove start of second
                                LeftPoints.addAll(bottomPoints);                   //add second to first
                                segments.remove(bottomConveyor);                       //delete second
                            }
                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeDown == 2 || typeDown == 9 | typeDown == 10)){
                            //one of them is good, join that segments
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){

                                ConveyorBeltSegment segment = getSegment(x - 1, y);

                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);

                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);

                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;
                    case 6://  LeftUp

                        if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) && (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            //then join em!
                            ConveyorBeltSegment LeftConveyor = getSegment(x - 1, y);
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);

                            if(LeftConveyor == topConveyor){
                                LeftConveyor.setEndPoint(x, y);
                                LeftConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> LeftPoints = LeftConveyor.getPoints();
                                ArrayList<Point> topPoints = topConveyor.getPoints();

                                LeftPoints.remove(LeftPoints.size() - 1);   //remove end of first
                                LeftPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                topPoints.remove(0);                     //remove start of second
                                LeftPoints.addAll(topPoints);                   //add second to first
                                segments.remove(topConveyor);                       //delete second
                            }

                        }else if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11) || (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            //then this segment joins one of them
                            if((typeLeft == 1 || typeLeft == 9 || typeLeft == 11)){
                                ConveyorBeltSegment segment = getSegment(x - 1, y);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }

                        break;
                    case 7://  RightDown
                        if((typeRight == 3 || typeRight == 10 || typeRight == 12) && (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);

                            if(rightConveyor == bottomConveyor){
                                rightConveyor.setEndPoint(x, y);
                                rightConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();

                                rightPoints.remove(rightPoints.size() - 1);   //remove end of first
                                rightPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                bottomPoints.remove(0);                     //remove start of second
                                rightPoints.addAll(bottomPoints);                   //add second to first
                                segments.remove(bottomConveyor);                       //delete second
                            }

                        }else if((typeRight == 3 || typeRight == 10 || typeRight == 12) || (typeDown == 2 || typeDown == 9 || typeDown == 10)){
                            if((typeRight == 3 || typeRight == 10 || typeRight == 12)){
                                //then join the right segment in pointing towards me
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;
                    case 8://  RightUp
                        if((typeRight == 3 || typeRight == 10 || typeRight == 12) && (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);

                            if(rightConveyor == topConveyor){
                                rightConveyor.setEndPoint(x, y);
                                rightConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();
                                ArrayList<Point> topPoints = topConveyor.getPoints();

                                rightPoints.remove(rightPoints.size() - 1);   //remove end of first
                                rightPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                topPoints.remove(0);                     //remove start of second
                                rightPoints.addAll(topPoints);                   //add second to first
                                segments.remove(topConveyor);                   //delete the second
                            }


                        }else if((typeRight == 3 || typeRight == 10 || typeRight == 12) || (typeUp == 4 || typeUp == 11 || typeUp == 12)){
                            if((typeRight == 3 || typeRight == 10 || typeRight == 12)){
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;


                    case 9://  UpRight
                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            //in the case of both good, connect!
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);

                            if(topConveyor == rightConveyor){
                                topConveyor.setEndPoint(x, y);
                                topConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> topPoints = topConveyor.getPoints();
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();

                                topPoints.remove(topPoints.size() - 1);   //remove end of first
                                topPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                rightPoints.remove(0);                     //remove start of second
                                topPoints.addAll(rightPoints);                   //add second to first
                                segments.remove(rightConveyor);                 //delete the second
                            }


                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if((typeUp == 2 || typeUp == 5 || typeUp == 7)){
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;
                    case 10://  UpLeft
                        if((typeUp == 2 || typeUp == 5 || typeUp == 7) && (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            ConveyorBeltSegment topConveyor = getSegment(x, y + 1);
                            ConveyorBeltSegment leftConveyor = getSegment(x - 1, y);

                            if(topConveyor == leftConveyor){
                                topConveyor.setEndPoint(x, y);
                                topConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> topPoints = topConveyor.getPoints();
                                ArrayList<Point> leftPoints = leftConveyor.getPoints();

                                topPoints.remove(topPoints.size() - 1);   //remove end of first
                                topPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                leftPoints.remove(0);                     //remove start of second
                                topPoints.addAll(leftPoints);                   //add second to first
                                segments.remove(leftConveyor);                  //delete second
                            }
                        }else if((typeUp == 2 || typeUp == 5 || typeUp == 7) || (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if((typeUp == 2 || typeUp == 5 || typeUp == 7)){
                                ConveyorBeltSegment segment = getSegment(x, y + 1);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x , y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x - 1, y);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;
                    case 11://  DownRight
                        if((typeDown == 4 || typeDown == 6 || typeDown == 8) && (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);
                            ConveyorBeltSegment rightConveyor = getSegment(x + 1, y);

                            if(bottomConveyor == rightConveyor){
                                bottomConveyor.setEndPoint(x, y);
                                bottomConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();
                                ArrayList<Point> rightPoints = rightConveyor.getPoints();

                                bottomPoints.remove(bottomPoints.size() - 1);   //remove end of first
                                bottomPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                rightPoints.remove(0);                     //remove start of second
                                bottomPoints.addAll(rightPoints);                   //add second to first
                                segments.remove(rightConveyor);                  //delete second
                            }

                        }else if((typeDown == 4 || typeDown == 6 || typeDown == 8) || (typeRight == 1 || typeRight == 5 || typeRight == 6)){
                            if((typeDown == 4 || typeDown == 6 || typeDown == 8)){
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.addPointRightBeforeEnd(x, y);
                                segment.setEndPoint(x, y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x + 1, y);
                                segment.addPointRightAfterStart(x, y);
                                segment.setStartPoint(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }
                        break;
                    case 12://  DownLeft

                        if((typeDown == 4 || typeDown == 6 || typeDown == 8) && (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            ConveyorBeltSegment bottomConveyor = getSegment(x, y - 1);
                            ConveyorBeltSegment leftConveyor = getSegment(x - 1, y);

                            if(bottomConveyor == leftConveyor){
                                bottomConveyor.setEndPoint(x, y);
                                bottomConveyor.addPointRightBeforeEnd(x, y);
                            }else{
                                ArrayList<Point> bottomPoints = bottomConveyor.getPoints();
                                ArrayList<Point> leftPoints = leftConveyor.getPoints();

                                bottomPoints.remove(bottomPoints.size() - 1);   //remove end of first
                                bottomPoints.add(new Point(x, y));                  //because we placed a curved section, add a new turn point
                                leftPoints.remove(0);                     //remove start of second
                                bottomPoints.addAll(leftPoints);                   //add second to first
                                segments.remove(leftConveyor);                  //delete second
                            }

                        }else if((typeDown == 4 || typeDown == 6 || typeDown == 8) || (typeLeft == 3 || typeLeft == 7 || typeLeft == 8)){
                            if((typeDown == 4 || typeDown == 6 || typeDown == 8)){
                                ConveyorBeltSegment segment = getSegment(x, y - 1);
                                segment.setEndPoint(x, y);
                                segment.addPointRightBeforeEnd(x, y);
                            }else{
                                ConveyorBeltSegment segment = getSegment(x - 1, y);
                                segment.setStartPoint(x, y);
                                segment.addPointRightAfterStart(x, y);
                            }
                        }else{
                            ConveyorBeltSegment seg = new ConveyorBeltSegment(new Point(x, y));
                            seg.addPointRightAfterStart(x, y);
                            segments.add(seg);
                        }

                        break;
                }
            }

            //now that we processed the change, actually change it
            if(isDelete){
                handler.getCurrentWorld().getBelts()[x][y] = 0;
            }else{
                handler.getCurrentWorld().getBelts()[x][y] = (byte) type;
            }


        }
        changes.clear();
        updateConveyorSegments = false;
    }

    private ConveyorBeltSegment getSegment(int x, int y){

        for(ConveyorBeltSegment segment : segments){
            //we will loop through all of the segments
            //if this point lies between any of 2 of the segment points, then its on

            //now get all of the points in this segment
            for(int i = 1; i < segment.getNumberOfPoints(); i++){

                Point lastPoint = segment.getPoint(i - 1);
                Point thisPoint = segment.getPoint(i);
                //now we want to determine if lastPoint and thisPoint are collinear horizontal or vertical

                if(lastPoint.x == thisPoint.x && lastPoint.y == thisPoint.y){
                    if(x == lastPoint.x && y == lastPoint.y)
                        return segment;
                }


                if(lastPoint.x == thisPoint.x){
                    //their X's are the same, therefore vertical
                    if(x == lastPoint.x){
                        //so this piece of the segment is in the vertical direction and we are in that line
                        if(lastPoint.y < thisPoint.y){
                            if(lastPoint.y <= y && y <= thisPoint.y)
                                return segment;
                        }else if(lastPoint.y > thisPoint.y){
                            if(thisPoint.y <= y && y <= lastPoint.y)
                                return segment;
                        }
                    }

                }else if(lastPoint.y == thisPoint.y){
                    //their Y's are the same therefore horizontal
                    if(y == lastPoint.y){//so this is a horizontal segment and we are on that line
                        //now check which is larger
                        if(lastPoint.x > thisPoint.x){
                            if(thisPoint.x <= x && x <= lastPoint.x)
                                return segment;
                        }else if(lastPoint.x < thisPoint.x){
                            if(lastPoint.x <= x && x <= thisPoint.x)
                                return segment;
                        }
                    }
                }

            }



        }
        //System.out.println("Return Null!");
        return null;
    }

    private boolean isLoop(ConveyorBeltSegment segment){
        /*
              okay, in order to determine if this is a loop, we are going to loop at the start block, and depending on its orientation, check the appropriate block,
              if thats appropriate, then its a loop?
         */

        Point start = segment.getStart();
        Point end = segment.getEnd();

        System.out.println("Checking start: (" + start.x + ", " + start.y + ")  -> (" + end.x + ", " + end.y + ")");

        byte startTYPE = handler.getCurrentWorld().getBelts()[start.x][start.y];
        byte endTYPE = handler.getCurrentWorld().getBelts()[end.x][end.y];

        System.out.println("types: " + startTYPE + " end " + endTYPE);

        switch (startTYPE){
            case 1: //Right
            case 5: //Left Down
            case 6: //Left Up
                //okay if start is a right, then check if the end block is to the left, and check if its the right type
                if((end.x == start.x - 1) && (end.y == start.y))
                    if(endTYPE == 1 || endTYPE == 9 || endTYPE == 11)
                        return true;
                break;

            case 2:
            case 9:
            case 10:
                if((end.x == start.x) && (end.y == start.y + 1))
                    if(endTYPE == 2 || endTYPE == 5 || endTYPE == 7)
                        return true;
                break;

            case 3:
            case 7:
            case 8:
                if((end.x == start.x + 1) && (end.y == start.y))
                    if(endTYPE == 3 || endTYPE == 10 || endTYPE == 12)
                        return true;
                break;

            case 4:
            case 11:
            case 12:
                if((end.x == start.x) && (end.y == start.y - 1))
                    if(endTYPE == 4 || endTYPE == 6 || endTYPE == 8)
                        return true;
                break;
        }

        return false;

    }

    public void addChange(int x, int y, int type, boolean isDelete){
        changes.add(new BeltChangeEvent(x, y, type, isDelete));
        updateConveyorSegments = true;
        System.out.println("BeltEvent: " + x + " , " + y + " , Type: " + type + " , isDelete: " + isDelete );
    }
    public int getSegmentCount(){
        return segments.size();
    }





}
