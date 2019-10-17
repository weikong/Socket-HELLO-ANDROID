package com.king.chat.socket.util;

import com.king.chat.socket.bean.CombineBitmapEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/10/17.
 */

public class CombineNineRect {

    public final static int imgWidth = DisplayUtil.dp2px(100);
    public final static int imgHeight = DisplayUtil.dp2px(100);
    public final static int itemSpace = DisplayUtil.dp2px(2);

    private static class CombineNineRectHolder{
        private static final CombineNineRect INSTANCE = new CombineNineRect();
    }

    /**
     * 单一实例
     */
    public static final CombineNineRect getInstance(){
        return CombineNineRect.CombineNineRectHolder.INSTANCE;
    }

    public List<CombineBitmapEntity> generateCombineBitmapEntity(int length){
        int itemWidth = imgWidth;
        List<CombineBitmapEntity> list = new ArrayList<>();
        switch (length){
            case 3:
                itemWidth = (imgWidth - 3*itemSpace) / 2;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = (imgWidth - itemWidth) / 2;
                        entity.y = itemSpace;
                    } else if (i == 1) {
                        entity.x = itemSpace;
                        entity.y = itemSpace * 2 + itemWidth;
                    } else if (i == 2) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = itemSpace * 2 + itemWidth;
                    }
                    list.add(entity);
                }
                break;
            case 4:
                itemWidth = (imgWidth - 3*itemSpace) / 2;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = itemSpace;
                        entity.y = itemSpace;
                    } else if (i == 1) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = itemSpace;
                    } else if (i == 2) {
                        entity.x = itemSpace;
                        entity.y = itemSpace * 2 + itemWidth;
                    } else if (i == 3) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = itemSpace * 2 + itemWidth;
                    }
                    list.add(entity);
                }
                break;
            case 5:
                itemWidth = (imgWidth - 4*itemSpace) / 3;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = (imgWidth - itemWidth * 2 - itemSpace) / 2;
                        entity.y = (imgHeight - itemWidth * 2 - itemSpace) / 2;
                    } else if (i == 1) {
                        entity.x = (imgWidth+itemSpace) / 2;
                        entity.y = (imgHeight - itemWidth * 2 - itemSpace) / 2;
                    } else if (i == 2) {
                        entity.x = itemSpace;
                        entity.y = (imgWidth+itemSpace) / 2;
                    } else if (i == 3) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = (imgWidth+itemSpace) / 2;
                    } else if (i == 4) {
                        entity.x = itemSpace * 3 + 2*itemWidth;
                        entity.y = (imgWidth+itemSpace) / 2;
                    }
                    list.add(entity);
                }
                break;
            case 6:
                itemWidth = (imgWidth - 4*itemSpace) / 3;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = itemSpace;
                        entity.y = (imgHeight - itemWidth * 2 - itemSpace) / 2;
                    } else if (i == 1) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = (imgHeight - itemWidth * 2 - itemSpace) / 2;
                    } else if (i == 2) {
                        entity.x = itemSpace * 3 + 2*itemWidth;
                        entity.y = (imgHeight - itemWidth * 2 - itemSpace) / 2;
                    } else if (i == 3) {
                        entity.x = itemSpace;
                        entity.y = (imgHeight+itemSpace) / 2;
                    } else if (i == 4) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = (imgHeight+itemSpace) / 2;
                    } else if (i == 5) {
                        entity.x = itemSpace * 3 + 2*itemWidth;
                        entity.y = (imgHeight+itemSpace) / 2;
                    }
                    list.add(entity);
                }
                break;
            case 7:
                itemWidth = (imgWidth - 4*itemSpace) / 3;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = (imgWidth - itemWidth * 2 - itemSpace) / 2;
                        entity.y = itemSpace;
                    } else if (i == 1) {
                        entity.x = (imgWidth+itemSpace) / 2;
                        entity.y = itemSpace;
                    } else if (i == 2) {
                        entity.x = itemSpace;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 3) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 4) {
                        entity.x = itemSpace * 3 + itemWidth * 2;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 5) {
                        entity.x = (imgWidth - itemWidth * 2 - itemSpace) / 2;
                        entity.y = itemWidth * 2 + itemSpace * 3;
                    } else if (i == 6) {
                        entity.x = (imgWidth+itemSpace) / 2;
                        entity.y = itemWidth * 2 + itemSpace * 3;
                    }
                    list.add(entity);
                }
                break;
            case 8:
                itemWidth = (imgWidth - 4*itemSpace) / 3;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = (imgWidth - itemWidth * 2 - itemSpace) / 2;
                        entity.y = itemSpace;
                    } else if (i == 1) {
                        entity.x = (imgWidth+itemSpace) / 2;
                        entity.y = itemSpace;
                    } else if (i == 2) {
                        entity.x = itemSpace;
                        entity.y = imgHeight - itemWidth - itemSpace * 2;
                    } else if (i == 3) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = imgHeight - itemWidth - itemSpace * 2;
                    } else if (i == 4) {
                        entity.x = itemSpace * 3 + itemWidth * 2;
                        entity.y = imgHeight - itemWidth - itemSpace * 2;
                    } else if (i == 5) {
                        entity.x = itemSpace;
                        entity.y = imgHeight - itemWidth * 2 - itemSpace * 3;
                    } else if (i == 6) {
                        entity.x = itemSpace * 2 + itemWidth;
                        entity.y = imgHeight - itemWidth * 2 - itemSpace * 3;
                    } else if (i == 7) {
                        entity.x = itemSpace * 3 + itemWidth * 2;
                        entity.y = imgHeight - itemWidth * 2 - itemSpace * 3;
                    }
                    list.add(entity);
                }
                break;
            case 9:
                itemWidth = (imgWidth - 4*itemSpace) / 3;
                for(int i=0;i<length;i++){
                    CombineBitmapEntity entity = new CombineBitmapEntity();
                    entity.width = itemWidth;
                    entity.height = itemWidth;
                    if (i == 0){
                        entity.x = itemSpace;
                        entity.y = itemSpace;
                    } else if (i == 1) {
                        entity.x = itemWidth + itemSpace * 2;
                        entity.y = itemSpace;
                    } else if (i == 2) {
                        entity.x = itemWidth * 2 + itemSpace * 3;
                        entity.y = itemSpace;
                    } else if (i == 3) {
                        entity.x = itemSpace;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 4) {
                        entity.x = itemWidth + itemSpace * 2;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 5) {
                        entity.x = itemWidth * 2 + itemSpace * 3;
                        entity.y = itemWidth + itemSpace * 2;
                    } else if (i == 6) {
                        entity.x = itemSpace;
                        entity.y = itemWidth * 2 + itemSpace * 3;
                    } else if (i == 7) {
                        entity.x = itemWidth + itemSpace * 2;
                        entity.y = itemWidth * 2 + itemSpace * 3;
                    } else if (i == 8) {
                        entity.x = itemWidth * 2 + itemSpace * 3;
                        entity.y = itemWidth * 2 + itemSpace * 3;
                    }
                    list.add(entity);
                }
                break;

        }
        return list;
    }
}
