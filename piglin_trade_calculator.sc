//猪灵交易效率计算器
//  作者：Cupperum29，Deepseek V4.1 Flash辅助
//  适配版本：1.16.2+
//
//     猪灵交易间隔 6 秒，即每 6 秒消耗 1 个金锭，猪灵产出 1 次产物
//     默认交易效率为 100%，因此本计算器为理论效率计算器
//     注意：由于猪灵交易的间隔较大，因此要尤其注意收集率
//     部分数据来自群友转发的计算表格
//     用法：piglin_trade_calculator <猪灵数量>
//     Data_Version:1.21.6->4435
//                            1.16.2->2576
__config() ->
  {
      'commands' ->
           {
               //成表输出猪灵交易效率表格
              '<count>' -> 'calc',                                  
               //单独输出指定物品的交易效率
              '<count> <item>' -> 'calc_item'
           },
                  'arguments' ->
                    {
                       'count' -> { 
                                    'type' -> 'int',  //整数类型，为猪灵数量
                                    'min' -> 1,       //最小值为 1，猪灵数量为0无意义，两个限制合为正整数
                                    'suggest' -> [120, 1728, 1890] //提供了几个常用选项
                                  },
                       'item' -> {'type' -> 'term',
                                  'options' -> [//options修饰后，只能选择以上物品，否则报错
                                                'gravel', //沙砾
                                                'blackstone', //黑石
                                                'spectral_arrow', //光灵箭
                                                'iron_nugget', //铁粒
                                                'nether_brick', //下界砖
                                                'soul_sand', //灵魂沙
                                                'quartz', //下界石英
                                                'leather', //皮革
                                                'string', //线
                                                'crying_obsidian',//哭泣黑曜石
                                                'fire_charge',//火焰弹
                                                'obsidian',// //黑曜石
                                                'ender_pearl',//末影珍珠
                                                'water_bottle',//水瓶
                                                'dried_ghast',//失水恶魂
                                                'splash_potion',//喷溅型抗火药水
                                                'potion',//抗火药水
                                                'iron_boots',//铁靴子
                                                'enchanted_book'//附魔书
                                               ]
                                  }
                    },
                  'scope' -> 'player'//让每个玩家的应用状态相互隔离
  };

                  //全局变量

                  //每个物品所需的平均金锭数量（版本>=1.21.6，多了个失水恶魂）,数据来自https://zh.minecraft.wiki/w/%E4%BB%A5%E7%89%A9%E6%98%93%E7%89%A9
                  global_rates_1_21_6 = 
                  {
                        'gravel' -> 0.977,
                        'blackstone' -> 0.977,
                        'spectral_arrow' -> 1.303,
                        'iron_nugget' -> 2.039,
                        'nether_brick' -> 2.345,
                        'soul_sand' -> 2.345,
                        'quartz' -> 2.759,
                        'leather' -> 3.908,
                        'string' -> 3.908,
                        'crying_obsidian' -> 5.8625,
                        'fire_charge' -> 11.725,
                        'obsidian' -> 11.725,
                        'ender_pearl' -> 15.63,
                        'water_bottle' -> 46.9,
                        'dried_ghast' -> 46.9,
                        'splash_potion' -> 58.625,
                        'potion' -> 58.625,
                        'iron_boots' -> 58.625,
                        'enchanted_book' -> 93.8,
                  };
                  //每个物品所需的平均金锭数量（版本1.16.2~1.21.5）,数据来自群友转发的计算表格
                  global_rates_1_16_2 = 
                  {
                        'gravel' -> 0.96,
                        'blackstone' -> 0.96,
                        'spectral_arrow' -> 1.275,
                        'iron_nugget' -> 1.996,
                        'nether_brick' -> 2.295,
                        'soul_sand' -> 2.295,
                        'quartz' ->  2.7,
                        'leather' -> 3.825,
                        'string' -> 3.825,
                        'crying_obsidian' -> 5.7375,
                        'fire_charge' -> 11.475,
                        'obsidian' -> 11.475,
                        'ender_pearl' -> 15.3,
                        'water_bottle' -> 45.9,
                        'splash_potion' -> 57.375,
                        'potion' -> 57.375,
                        'iron_boots' -> 57.375,
                        'enchanted_book' -> 91.8,
                  };
                  //‼️不考虑版本1.16.2以下的版本‼️

                  //物品名字映射表
                  global_name = 
                  {
                        'gravel' -> '沙砾',
                        'blackstone' -> '黑石',
                        'spectral_arrow' -> '光灵箭',
                        'iron_nugget' -> '铁粒',
                        'nether_brick' -> '下界砖',
                        'soul_sand' -> '灵魂沙',
                        'quartz' -> '下界石英',
                        'leather' -> '皮革',
                        'string' -> '线',
                        'crying_obsidian' -> '哭泣黑曜石',
                        'fire_charge' -> '火焰弹',
                        'obsidian' -> '黑曜石',
                        'ender_pearl' -> '末影珍珠',
                        'water_bottle' -> '水瓶',
                        'dried_ghast' -> '失水恶魂',
                        'splash_potion' -> '喷溅型抗火药水',
                        'potion' -> '抗火药水',
                        'iron_boots' -> '铁靴子',
                        'enchanted_book' -> '附魔书'
                  };
                  //物品颜色映射表
                  global_color = 
                  {
                        'gravel' -> '#4D4C4C',
                        'blackstone' -> '#1D1A20',
                        'spectral_arrow' -> '#DAB930',
                        'iron_nugget' -> '#F2F2F2',
                        'nether_brick' -> '#1D0E11',
                        'soul_sand' -> '#49372C',
                        'quartz' -> '#DBD1C1',
                        'leather' -> '#C65C35',
                        'string' -> '#F7F7F7',
                        'crying_obsidian' -> '#6006AB',
                        'fire_charge' -> '#EEAC18',
                        'obsidian' -> '#3B2754',
                        'ender_pearl' -> '#105E51',
                        'water_bottle' -> '#2B4899',
                        'dried_ghast' -> '#665D5D',
                        'splash_potion' -> '#C57600',
                        'potion' -> '#C57600',
                        'iron_boots' -> '#D8D8D8',
                        'enchanted_book' -> '#3B2765'
                  };

                  global_ts = 6;//猪灵交易间隔，单位秒

             //判断版本并且计算当前版本下每种产物的平均效率、总效率并且列表输出结果
                  calc(count) ->
                  (
                      dv = system_info('game_data_version');//获取当前游戏数据版本（1.16.2->2578,1.21.5->4325,1.21.6->4435）

                      //判断游戏版本,根据版本计算对应版本的平均效率
                      if
                         (
                           dv >= 4435,              // 1.21.6+
                           (
                             //计算1.21.6+的平均效率
                             print(player(), format('w 当前的游戏版本为1.21.6+'));
                             gravel = count * 3600 / global_ts /global_rates_1_21_6:'gravel';//沙砾的效率
                             blackstone = count * 3600 / global_ts / global_rates_1_21_6:'blackstone';//黑石的效率
                             spectral_arrow = count * 3600 / global_ts / global_rates_1_21_6:'spectral_arrow';//光灵箭的效率
                             iron_nugget = count * 3600 / global_ts / global_rates_1_21_6:'iron_nugget';//铁粒的效率
                             nether_brick = count * 3600 / global_ts / global_rates_1_21_6:'nether_brick';//下界砖的效率
                             soul_sand = count * 3600 / global_ts / global_rates_1_21_6:'soul_sand';//灵魂沙的效率
                             quartz = count * 3600 / global_ts / global_rates_1_21_6:'quartz';//下界石英的效率
                             leather = count * 3600 / global_ts / global_rates_1_21_6:'leather';//皮革的效率
                             string = count * 3600 / global_ts / global_rates_1_21_6:'string';//线的效率
                             crying_obsidian = count * 3600 / global_ts / global_rates_1_21_6:'crying_obsidian';//哭泣黑曜石的效率
                             fire_charge = count * 3600 / global_ts / global_rates_1_21_6:'fire_charge';//火焰弹的效率
                             obsidian = count * 3600 / global_ts / global_rates_1_21_6:'obsidian';//黑曜石的效率
                             ender_pearl = count * 3600 / global_ts / global_rates_1_21_6:'ender_pearl';//末影珍珠的效率
                             water_bottle = count * 3600 / global_ts / global_rates_1_21_6:'water_bottle';//水瓶的效率
                             dried_ghast = count * 3600 / global_ts / global_rates_1_21_6:'dried_ghast';//失水恶魂的效率
                             splash_potion = count * 3600 / global_ts / global_rates_1_21_6:'splash_potion';//喷溅型抗火药水的效率
                             potion = count * 3600 / global_ts / global_rates_1_21_6:'potion';//抗火药水的效率
                             iron_boots = count * 3600 / global_ts / global_rates_1_21_6:'iron_boots';//铁靴子的效率
                             enchanted_book = count * 3600 / global_ts / global_rates_1_21_6:'enchanted_book';//附魔书的效率
                           ),
                                                          // 1.16.2~1.21.5
                           (
                             //计算1.16.2~1.21.5的平均效率
                             dried_ghast = 0;//不包括失水恶魂
                             print(player(), format('w 当前的游戏版本为1.16.2~1.21.5'));
                             gravel = count * 3600 / global_ts / global_rates_1_16_2:'gravel';//沙砾的效率
                             blackstone = count * 3600 / global_ts / global_rates_1_16_2:'blackstone';//黑石的效率
                             spectral_arrow = count * 3600 / global_ts / global_rates_1_16_2:'spectral_arrow';//光灵箭的效率
                             iron_nugget = count * 3600 / global_ts / global_rates_1_16_2:'iron_nugget';//铁粒的效率
                             nether_brick = count * 3600 / global_ts / global_rates_1_16_2:'nether_brick';//下界砖的效率
                             soul_sand = count * 3600 / global_ts / global_rates_1_16_2:'soul_sand';//灵魂沙的效率
                             quartz = count * 3600 / global_ts / global_rates_1_16_2:'quartz';//下界石英的效率
                             leather = count * 3600 / global_ts / global_rates_1_16_2:'leather';//皮革的效率
                             string = count * 3600 / global_ts / global_rates_1_16_2:'string';//线的效率
                             crying_obsidian = count * 3600 / global_ts / global_rates_1_16_2:'crying_obsidian';//哭泣黑曜石的效率
                             fire_charge = count * 3600 / global_ts / global_rates_1_16_2:'fire_charge';//火焰弹的效率
                             obsidian = count * 3600 / global_ts / global_rates_1_16_2:'obsidian';//黑曜石的效率
                             ender_pearl = count * 3600 / global_ts / global_rates_1_16_2:'ender_pearl';//末影珍珠的效率
                             water_bottle = count * 3600 / global_ts / global_rates_1_16_2:'water_bottle';//水瓶的效率
                             splash_potion = count * 3600 / global_ts / global_rates_1_16_2:'splash_potion';//喷溅型抗火药水的效率
                             potion = count * 3600 / global_ts / global_rates_1_16_2:'potion';//抗火药水的效率
                             iron_boots = count * 3600 / global_ts / global_rates_1_16_2:'iron_boots';//铁靴子的效率
                             enchanted_book = count * 3600 / global_ts / global_rates_1_16_2:'enchanted_book';//附魔书的效率
                           )
                         );

                      // 求总效率
                      total = gravel + blackstone + spectral_arrow + iron_nugget + nether_brick + soul_sand + quartz
                      + leather + string + crying_obsidian + fire_charge + obsidian + ender_pearl + dried_ghast
                      + water_bottle + splash_potion + potion + iron_boots + enchanted_book;

                      // 打印输出并给对应物品的名称染色
                      print(player(), format('w 经过计算，', '#E62008 ' + str('%.0f', count)+'猪', 'w 的理论总平均效率为', '#E62008 ' + str('%.1f', total) +'/h'));                                                 //总效率
                      print(player(), format('#AAAAAA - ', global_color:'gravel' + ' ' + global_name:'gravel', '#AAAAAA ：','w ' + str('%.1f', gravel) +'/h'));                                                    //沙砾的效率
                      print(player(), format('#AAAAAA - ', global_color:'blackstone' + ' ' + global_name:'blackstone', '#AAAAAA ：','w ' + str('%.1f', blackstone) +'/h'));                              //黑石的效率
                      print(player(), format('#AAAAAA - ', global_color:'spectral_arrow' + ' ' + global_name:'spectral_arrow', '#AAAAAA ：','w ' + str('%.1f', spectral_arrow) +'/h'));        //光灵箭的效率
                      print(player(), format('#AAAAAA - ', global_color:'iron_nugget' + ' ' + global_name:'iron_nugget', '#AAAAAA ：','w ' + str('%.1f', iron_nugget) +'/h'));                        //铁粒的效率
                      print(player(), format('#AAAAAA - ', global_color:'nether_brick' + ' ' + global_name:'nether_brick', '#AAAAAA ：','w ' + str('%.1f', nether_brick) +'/h'));                    //下界砖的效率
                      print(player(), format('#AAAAAA - ', global_color:'soul_sand' + ' ' + global_name:'soul_sand', '#AAAAAA ：','w ' + str('%.1f', soul_sand) +'/h'));                                  //灵魂沙的效率
                      print(player(), format('#AAAAAA - ', global_color:'quartz' + ' ' + global_name:'quartz', '#AAAAAA ：','w ' + str('%.1f', quartz) +'/h'));                                                   //下界石英的效率
                      print(player(), format('#AAAAAA - ', global_color:'leather' + ' ' + global_name:'leather', '#AAAAAA ：','w ' + str('%.1f', leather) +'/h'));                                                //皮革的效率
                      print(player(), format('#AAAAAA - ', global_color:'string' + ' ' + global_name:'string', '#AAAAAA ：','w ' + str('%.1f', string) +'/h'));                                                      //线的效率
                      print(player(), format('#AAAAAA - ', global_color:'crying_obsidian' + ' ' + global_name:'crying_obsidian', '#AAAAAA ：','w ' + str('%.1f', crying_obsidian) +'/h'));     //哭泣黑曜石的效率
                      print(player(), format('#AAAAAA - ', global_color:'fire_charge' + ' ' + global_name:'fire_charge', '#AAAAAA ：','w ' + str('%.1f', fire_charge) +'/h'));                          //火焰弹的效率
                      print(player(), format('#AAAAAA - ', global_color:'obsidian' + ' ' + global_name:'obsidian', '#AAAAAA ：','w ' + str('%.1f', obsidian) +'/h'));                                          //黑曜石的效率
                      print(player(), format('#AAAAAA - ', global_color:'ender_pearl' + ' ' + global_name:'ender_pearl', '#AAAAAA ：','w ' + str('%.1f', ender_pearl) +'/h'));                        //末影珍珠的效率
                      print(player(), format('#AAAAAA - ', global_color:'water_bottle' + ' ' + global_name:'water_bottle', '#AAAAAA ：','w ' + str('%.1f', water_bottle) +'/h'));                    //水瓶的效率
                      if(dv >= 4435,print(player(), format('#AAAAAA - ', global_color:'dried_ghast' + ' ' + global_name:'dried_ghast', '#AAAAAA ：','w ' + str('%.1f', dried_ghast) +'/h'))); //失水恶魂的效率
                      print(player(), format('#AAAAAA - ', global_color:'splash_potion' + ' ' + global_name:'splash_potion', '#AAAAAA ：','w ' + str('%.1f', splash_potion) +'/h'));                //喷溅型抗火药水的效率
                      print(player(), format('#AAAAAA - ', global_color:'potion' + ' ' + global_name:'potion', '#AAAAAA ：','w ' + str('%.1f', potion) +'/h'));                                                    //抗火药水的效率
                      print(player(), format('#AAAAAA - ', global_color:'iron_boots' + ' ' + global_name:'iron_boots', '#AAAAAA ：','w ' + str('%.1f', iron_boots) +'/h'));                               //铁靴子的效率
                      print(player(), format('#AAAAAA - ', global_color:'enchanted_book' + ' ' + global_name:'enchanted_book', '#AAAAAA ：','w ' + str('%.1f', enchanted_book) +'/h'));    //附魔书的效率
                  );

             // 计算指定物品的效率
                  calc_item(count, item) ->
                  (
                      dv = system_info('game_data_version');//获取游戏的游戏数据版本，1.21.5为4322，1.21.6为4435
                      if
                        (
                          dv >= 4435,
                            (
                             print(player(), format('w 游戏版本为1.21.6+'));
                             eff = count * 3600 / global_ts / global_rates_1_21_6:item;//计算指定物品的效率
                             print(player(), format('w ' + count + '猪的', global_color:item + ' ' + global_name:item, 'w 的理论平均效率为' + str('%.1f', eff) +'/h'));
                            ),
                          (
                           if(item = dried_ghast),(print(player(), format('w ‼️1.21.6以下没有',global_color:item + ' 失水恶魂' + 'w ‼️'))),         
                           //谁会在没有失水恶魂的版本算失水恶魂的效率啊
                           ( 
                           print(player(), format('w 游戏版本为1.16.2~1.21.5'));
                           eff = count * 3600 / global_ts / global_rates_1_16_2:item;//计算指定物品的效率
                           print(player(), format('w ' + count + '猪的', global_color:item + ' ' + global_name:item, 'w 的理论平均效率为' + str('%.1f', eff) +'/h'));
                           )
                          )
                        );
                  );