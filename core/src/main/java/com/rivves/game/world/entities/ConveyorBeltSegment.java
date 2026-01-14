package com.rivves.game.world.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.world.blocks.Block;

import java.awt.*;
import java.util.ArrayList;

public class ConveyorBeltSegment {

    private ArrayList<Point> points;        //These are in order of rotation!

    private ArrayList<Item> items;
    private static BitmapFont font = AssetLoader.am.get("ui/fonts/consolas.fnt", BitmapFont.class);


    private boolean UpdateBeltSegmentsFlag = false;
    private boolean updateItemsFlag = false;            //set to true when we want the items to check themselves
    //private boolean isClosedLoop = false;

    public ConveyorBeltSegment(Point start){
        points = new ArrayList<>();
        points.add(start);  //its assumed that if the length is 1, than start and end are same spot
        points.add(start);  //every rail has a start and end
        System.out.println("New Conveyor Segment " + start + ", " + start);
    }

    public ConveyorBeltSegment(ArrayList<Point> points){
        this.points = points;
        System.out.println("New Conveyor Segment with Points[]  = " + points);
    }


    public void tick(){
        //tick the conveyorBeltSegment
    }

    public void renderDebugBatch(SpriteBatch sb){
        //sb.setColor(Color.BLUE);
        String builder = points.size() + ": ";
        for(Point p : points){
            builder += " (" + p.x + "," + p.y + ")";
        }
        font.draw(sb, builder, (points.get(0).x) * Block.BlockWidth, ((points.get(0).y + 1) * Block.BlockHeight) + 16);
    }
    public void renderDebug(ShapeRenderer sr){
        //lets draw a blue square over start
        //lets draw a red square over end
        //lets draw green over rotations
        // let highlight the whole thing in purple



        sr.setColor(Color.BLUE);
        sr.rect(points.get(0).x * Block.BlockWidth + 16, points.get(0).y * Block.BlockHeight + 16, 32, 32);

        sr.setColor(Color.RED);
        sr.rect(points.get(points.size() - 1).x * Block.BlockWidth + 16, points.get(points.size() - 1).y * Block.BlockHeight + 16, 32, 32);

        sr.setColor(Color.GREEN);
        for(int i = 1; i < points.size() - 1; i++){
            sr.rect(points.get(i).x * Block.BlockWidth + 24, points.get(i).y * Block.BlockHeight + 24, 16, 16);
        }
        //renderHover(sr);
    }
    public void renderHover(ShapeRenderer sr){
        sr.setColor(Color.VIOLET);

        for(int i = 1; i < points.size(); i++){
            //for every point, starting at the 2nd, renderbetween the last point and this point a rectangle
            //first determine if this points are horizontal or vertical, must be one of the two

            if(points.get(i-1).x == points.get(i).x){
                //so the Xs are equal, therefore vertical
                if(points.get(i - 1).y < points.get(i).y){
                    sr.rect(points.get(i - 1).x * Block.BlockWidth + 28, points.get(i).y * Block.BlockHeight, 8, (points.get(i).y - points.get(i - 1).y - 1) * -Block.BlockHeight);
                }else if(points.get(i - 1).y > points.get(i).y){
                    sr.rect(points.get(i - 1).x * Block.BlockWidth + 28, points.get(i - 1).y * Block.BlockHeight, 8, (points.get(i - 1).y - points.get(i).y - 1) * -Block.BlockHeight);
                }
            }else{
                //the X's arnt equal, therefore Ys are, therefore horizontal
                //then which ever is smaller
                if(points.get(i - 1).x < points.get(i).x){
                    //left to rightds
                    sr.rect((points.get(i - 1).x + 1) * Block.BlockWidth, points.get(i).y * Block.BlockHeight + 28, (points.get(i).x - points.get(i - 1).x - 1) * Block.BlockWidth, 8);
                }else if(points.get(i - 1).x > points.get(i).x){
                    //right to left
                    sr.rect((points.get(i).x + 1) * Block.BlockWidth, points.get(i).y * Block.BlockHeight + 28, (points.get(i - 1).x - points.get(i).x - 1) * Block.BlockWidth, 8);
                }
            }





        }


    }













    public void addStart(int x, int y){
        points.add(0, new Point(x, y));
    }
    public void addEnd(int x, int y){
        points.add(points.size(), new Point(x, y));
    }
    public void setStartPoint(int x, int y){
        points.set(0, new Point(x, y));
    }
    public void setEndPoint(int x, int y){
        points.set(points.size() - 1, new Point(x, y));
    }
    public Point getStart(){
        return points.get(0);
    }
    public void addPoint(int index, int x, int y){
        points.add(index, new Point(x, y));
    }
    public void removePoint(int x, int y){
        points.remove(new Point(x, y));
    }
    public Point getEnd(){
        return points.get(points.size() - 1);
    }
    public void removeStartAndEnd(){
        points.remove(0);
        points.remove(points.size() - 1);
    }


    public int getIndexJustBefore(int x, int y){
        /*
                this functions returns the index just 'before' the block in question
                used when determining where to cut the array for splitting a segment into 2 pieces

                to determine this, loop through all of the segments,

         */
        //we will loop through all of the segments
        //if this point lies between any of 2 of the segment points, then its on

        //now get all of the points in this segment

        for(int i = 1; i < points.size(); i++){

            Point lastPoint = points.get(i - 1);
            Point thisPoint = points.get(i);
            //now we want to determine if lastPoint and thisPoint are collinear horizontal or vertical


            if(lastPoint.x == thisPoint.x){
                //their X's are the same, therefore vertical
                if(x == lastPoint.x){
                    //so this piece of the segment is in the vertical direction and we are in that line
                    if(lastPoint.y < thisPoint.y){
                        if(lastPoint.y <= y && y <= thisPoint.y)
                            return (i - 1);
                    }else if(lastPoint.y > thisPoint.y){
                        if(thisPoint.y <= y && y <= lastPoint.y)
                            return (i - 1);
                    }
                }

            }else if(lastPoint.y == thisPoint.y){
                //their Y's are the same therefore horizontal
                if(y == lastPoint.y){//so this is a horizontal segment and we are on that line
                    //now check which is larger
                    if(lastPoint.x > thisPoint.x){
                        if(thisPoint.x <= x && x <= lastPoint.x)
                            return (i - 1);
                    }else if(lastPoint.x < thisPoint.x){
                        if(lastPoint.x <= x && x <= thisPoint.x)
                            return (i - 1);
                    }
                }
            }

        }

        return -1;      //if -1, then no index
    }
    public void setPoints(ArrayList<Point> points){
        this.points = points;
    }
    public Point getPoint(int index){
        if(index > points.size()){
            System.out.println("[ATTEMPT] ConveyorBeltSegment.getPoint trying to get index that doesnt exist " + index);
            return null;
        }else{
            return points.get(index);
        }
    }
    public void addPointRightAfterStart(int x, int y){
        points.add(1, new Point(x, y));
    }
    public void addPointRightBeforeEnd(int x, int y){
        points.add(points.size() - 1, new Point(x, y));//shift the end point by 1
    }
    public int getNumberOfPoints(){
        return points.size();
    }


    public ArrayList<Point> getPoints() {
        return points;
    }
}
