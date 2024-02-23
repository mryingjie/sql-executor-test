package com.heitaox.sql.executor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.heitaox.sql.executor.test.demo.TreeStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yingjie Zheng
 * @date 2023/5/26 15:07
 * @description
 */
public class CategoryPermission {

    public static void main(String[] args) {
        String s = " [\n" +
                "        {\n" +
                "            \"name\": \"食品饮料\",\n" +
                "            \"categoryId\": \"533753759862789\",\n" +
                "            \"enable\": 1,\n" +
                "            \"parentId\": \"\",\n" +
                "            \"parentIdLine\": \"\",\n" +
                "            \"deleted\": null,\n" +
                "            \"createdTime\": \"2023-05-29T18:20:36\",\n" +
                "            \"updatedTime\": \"2023-05-30T14:53:49\",\n" +
                "            \"level\": 1,\n" +
                "            \"channelCode\": null,\n" +
                "            \"categoryCode\": \"\",\n" +
                "            \"childCategory\": [\n" +
                "                {\n" +
                "                    \"name\": \"茶\",\n" +
                "                    \"categoryId\": \"533753765302329\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:36\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:32\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"传统滋补营养品\",\n" +
                "                    \"categoryId\": \"533754000576545\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:40\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:05\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"冲饮\",\n" +
                "                    \"categoryId\": \"533754061787188\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:41\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:23\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"粮油米面\",\n" +
                "                    \"categoryId\": \"533754210881559\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:43\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:37\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"调味品\",\n" +
                "                    \"categoryId\": \"533754514575394\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:47\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:33\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"南北干货\",\n" +
                "                    \"categoryId\": \"533754952421402\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:54\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:25\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"零食坚果\",\n" +
                "                    \"categoryId\": \"533755169542156\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:20:57\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:39\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"预制菜\",\n" +
                "                    \"categoryId\": \"533755922485266\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:09\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:36\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"蛋及蛋制品\",\n" +
                "                    \"categoryId\": \"533757484142607\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:33\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:37:47\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"海鲜水产\",\n" +
                "                    \"categoryId\": \"533757599748195\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:35\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:34\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"蔬菜\",\n" +
                "                    \"categoryId\": \"533757774008367\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:37\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:32\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"水果\",\n" +
                "                    \"categoryId\": \"533757982609471\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:40\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:15\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"猪牛羊禽肉及制品\",\n" +
                "                    \"categoryId\": \"533758616997949\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:21:50\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:26\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"水饮\",\n" +
                "                    \"categoryId\": \"533759560061012\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:22:05\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:34\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"冰淇淋\",\n" +
                "                    \"categoryId\": \"533760727257182\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:22:22\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:36:57\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"乳制品\",\n" +
                "                    \"categoryId\": \"533760892538896\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:22:25\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:33\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"酒类\",\n" +
                "                    \"categoryId\": \"533795396522076\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533753759862789\",\n" +
                "                    \"parentIdLine\": \"533753759862789\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:11\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:28\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"日用品\",\n" +
                "            \"categoryId\": \"533760926289934\",\n" +
                "            \"enable\": 1,\n" +
                "            \"parentId\": \"\",\n" +
                "            \"parentIdLine\": \"\",\n" +
                "            \"deleted\": null,\n" +
                "            \"createdTime\": \"2023-05-29T18:22:25\",\n" +
                "            \"updatedTime\": \"2023-05-30T14:53:49\",\n" +
                "            \"level\": 1,\n" +
                "            \"channelCode\": null,\n" +
                "            \"categoryCode\": \"\",\n" +
                "            \"childCategory\": [\n" +
                "                {\n" +
                "                    \"name\": \"3C数码\",\n" +
                "                    \"categoryId\": \"533760932253774\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:22:25\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:25\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"服饰内衣\",\n" +
                "                    \"categoryId\": \"533763063418888\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:22:58\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:33:08\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"个护家清\",\n" +
                "                    \"categoryId\": \"533767640584241\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:24:08\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:35\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"美妆\",\n" +
                "                    \"categoryId\": \"533772233478210\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:25:18\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:34\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"家居用品\",\n" +
                "                    \"categoryId\": \"533774101057621\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:25:46\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:33:02\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"家用电器\",\n" +
                "                    \"categoryId\": \"533779647041607\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:27:11\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:25\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"母婴宠物\",\n" +
                "                    \"categoryId\": \"533783784853555\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:28:14\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:38:36\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"鞋靴箱包\",\n" +
                "                    \"categoryId\": \"533786984976472\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:29:03\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:40\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"虚拟\",\n" +
                "                    \"categoryId\": \"533789426978885\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:29:40\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:03\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"运动户外\",\n" +
                "                    \"categoryId\": \"533789868036177\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:29:47\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:33:00\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"文创文具\",\n" +
                "                    \"categoryId\": \"533791384342582\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:10\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:28\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"玩具乐器\",\n" +
                "                    \"categoryId\": \"533792994627667\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:35\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:31:49\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"珠宝文玩\",\n" +
                "                    \"categoryId\": \"533793827131461\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:47\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:31:51\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"钟表配饰\",\n" +
                "                    \"categoryId\": \"533793928515682\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:49\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:32:37\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"其它\",\n" +
                "                    \"categoryId\": \"533794291716191\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:54\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:30:54\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"奢侈品\",\n" +
                "                    \"categoryId\": \"533803146285150\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533760926289934\",\n" +
                "                    \"parentIdLine\": \"533760926289934\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:33:10\",\n" +
                "                    \"updatedTime\": \"2023-05-29T18:33:12\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"图书\",\n" +
                "            \"categoryId\": \"533794308427797\",\n" +
                "            \"enable\": 1,\n" +
                "            \"parentId\": \"\",\n" +
                "            \"parentIdLine\": \"\",\n" +
                "            \"deleted\": null,\n" +
                "            \"createdTime\": \"2023-05-29T18:30:55\",\n" +
                "            \"updatedTime\": \"2023-06-21T14:03:55\",\n" +
                "            \"level\": 1,\n" +
                "            \"channelCode\": null,\n" +
                "            \"categoryCode\": \"\",\n" +
                "            \"childCategory\": [\n" +
                "                {\n" +
                "                    \"name\": \"教育\",\n" +
                "                    \"categoryId\": \"533794312556578\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:55\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:16\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"文艺\",\n" +
                "                    \"categoryId\": \"533794407452757\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:56\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:18\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"童书\",\n" +
                "                    \"categoryId\": \"533794568015969\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:30:59\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:19\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"人文社科\",\n" +
                "                    \"categoryId\": \"533794690961501\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:01\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:20\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"生活\",\n" +
                "                    \"categoryId\": \"533794847264860\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:03\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:23\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"科技\",\n" +
                "                    \"categoryId\": \"533795026702392\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:06\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:24\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"经管\",\n" +
                "                    \"categoryId\": \"533795263418464\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:09\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:26\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"期刊\",\n" +
                "                    \"categoryId\": \"533795339440151\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:10\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:27\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"文创\",\n" +
                "                    \"categoryId\": \"533795356872790\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:11\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:30\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"其它\",\n" +
                "                    \"categoryId\": \"533795376861192\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:31:11\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:31\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"励志\",\n" +
                "                    \"categoryId\": \"533809935487013\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:34:53\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:32\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"进口书\",\n" +
                "                    \"categoryId\": \"533815465349187\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"533794308427797\",\n" +
                "                    \"parentIdLine\": \"533794308427797\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-05-29T18:36:18\",\n" +
                "                    \"updatedTime\": \"2023-05-30T16:09:33\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"本地生活\",\n" +
                "            \"categoryId\": \"1104846433972320\",\n" +
                "            \"enable\": 1,\n" +
                "            \"parentId\": \"\",\n" +
                "            \"parentIdLine\": \"\",\n" +
                "            \"deleted\": null,\n" +
                "            \"createdTime\": \"2023-09-07T14:56:59\",\n" +
                "            \"updatedTime\": \"2023-09-07T14:56:59\",\n" +
                "            \"level\": 1,\n" +
                "            \"channelCode\": null,\n" +
                "            \"categoryCode\": \"\",\n" +
                "            \"childCategory\": [\n" +
                "                {\n" +
                "                    \"name\": \"餐饮\",\n" +
                "                    \"categoryId\": \"1104848244727869\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"1104846433972320\",\n" +
                "                    \"parentIdLine\": \"1104846433972320\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-09-07T14:57:26\",\n" +
                "                    \"updatedTime\": \"2023-09-07T14:57:26\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"到店综合\",\n" +
                "                    \"categoryId\": \"1104849779957826\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"1104846433972320\",\n" +
                "                    \"parentIdLine\": \"1104846433972320\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-09-07T14:57:50\",\n" +
                "                    \"updatedTime\": \"2023-09-07T14:57:50\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"门票\",\n" +
                "                    \"categoryId\": \"1104850544844855\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"1104846433972320\",\n" +
                "                    \"parentIdLine\": \"1104846433972320\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-09-07T14:58:01\",\n" +
                "                    \"updatedTime\": \"2023-09-07T14:58:01\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"线路\",\n" +
                "                    \"categoryId\": \"1104850921656390\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"1104846433972320\",\n" +
                "                    \"parentIdLine\": \"1104846433972320\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-09-07T14:58:07\",\n" +
                "                    \"updatedTime\": \"2023-09-07T14:58:07\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"住宿\",\n" +
                "                    \"categoryId\": \"1104851651207231\",\n" +
                "                    \"enable\": 1,\n" +
                "                    \"parentId\": \"1104846433972320\",\n" +
                "                    \"parentIdLine\": \"1104846433972320\",\n" +
                "                    \"deleted\": null,\n" +
                "                    \"createdTime\": \"2023-09-07T14:58:18\",\n" +
                "                    \"updatedTime\": \"2023-09-07T14:58:18\",\n" +
                "                    \"level\": 2,\n" +
                "                    \"channelCode\": null,\n" +
                "                    \"categoryCode\": \"\",\n" +
                "                    \"childCategory\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]";
                List<TreeStructure> configMenus = new ArrayList<>();
        List<TreeStructure> result = new ArrayList<>();
        List<Map<String, Object>> maps = JSON.parseObject(s, new TypeReference<List<Map<String, Object>>>() {
        });

        for (Map<String, Object> map : maps) {
            TreeStructure configMenu = new TreeStructure();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (Objects.equals(key, "name")) {
                    configMenu.setDesc(entry.getValue().toString());
                }
                if (Objects.equals(key, "categoryId")) {
                    configMenu.setType(entry.getValue().toString());
                }
                if (Objects.equals(key, "childCategory")) {
                    List<TreeStructure> child = new ArrayList<>();
                    if (Objects.nonNull(entry.getValue())) {
                        List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
                        for (Map<String, Object> childMap : value) {
                            TreeStructure configMenuChild = new TreeStructure();
                            for (Map.Entry<String, Object> childEntry : childMap.entrySet()) {
                                String childEntryKey = childEntry.getKey();
                                if (Objects.equals(childEntryKey, "name")) {
                                    configMenuChild.setDesc(childEntry.getValue().toString());
                                }
                                if (Objects.equals(childEntryKey, "categoryId")) {
                                    configMenuChild.setType(childEntry.getValue().toString());
                                    System.out.println(configMenuChild.getType());
                                }
                                configMenuChild.setNext(new ArrayList<>());
                            }
                            child.add(configMenuChild);
                        }
                    }
                    configMenu.setNext(child);
                }
            }
            result.add(configMenu);
        }


        System.out.println(JSON.toJSONString(result));
    }

}

