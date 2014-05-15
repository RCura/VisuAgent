breed [groups group]
groups-own [energy-level technical-level distance-done age continent has-played? culture
  last-time-has-moved
  last-time-has-exchanged
  has-to-deal-with-migration
  has-to-deal-with-accumulation
  has-to-deal-with-innovation
  has-to-deal-with-exchange
  has-to-deal-with-survival  
  has-to-deal-with-ecological-impacts
  distanceDepart
  distanceDepartCarre
  father
  ]
patches-own [continent-name initial-ressource-level ressource-level ressource-to-recover listNeighbors littoral pattern countpresence]
globals [
  eventBirth
  eventDeath
  eventTechPlus
  eventTechDiffusion
  eventTechMinus
  eventMoveNoReason
  eventMoveAndAlone
  eventMoveAndTwoMore
  eventDontMove
  eventGoToSea
  eventBackToContinent
  eventIslandArrival
  eventMoveR
  eventMoveL
  eventMoveU
  eventMoveB
 typeMove
  ;proba-innovation-sensitivity
  ;Energy-accumulation-per-timestep-sensitivity
  listeCouleurs total-distance previous-total-distance listeContinents listePopContinents listeRessourcesContinents maxtech
  nombreGroupesAyantVecu distanceTotaleParcourue nombreGroupesAyantTraverse maxTechAtteint nombreGroupesALaFin nombreCellulesOccupees nombreRegionsOccupees
  distanceMoyenneDepart distanceMinDepart distanceMaxDepart distanceStandardDeviationDepart nombreGroupesSurlIle mintech meantech mediantech standard-deviationtech ageMoyenGroupes
  fractLittoral nIterations nombreVaguesEffectuees randomWalkDist
  groups-weight
  Initial-population
  Carrying-capacity-consommee
  Energy-consumption-per-move
  Energy-accumulation-per-timestep
  Innovation-probability
  Diffusion-probability 
  initialEnergy
  maximum-energy
  probaPerturbation
  impact-perturbation
  feel-lonely-time
  ProbaPartirSansRaison
  plot?
  displayCells?
  cornerconfiguration
  deal-with-exchanges 
  displayTechnicalOrColor
  robinet?
  natalite-probability
  angle-vision
  water-penalty
  initial-ressource-patches
  resource-regrow-per-step
  proba-aller-en-mer
  porteeVoisinageLittoral
  refreshTime
  groupesParVague
  nombreVagues
  ressource-regrow-per-step
  tempsEntreDeuxVagues
  differenciation?
  technologie?
  demographie?
  perturbations?
  techPlus
  techMinus
  perturb-tau
  gestation-time
  ]


; liberer les groupes sur l'Eldorado

;; ---------------------------------------------------------------------------------------
;; -----------------s----------------------------------------------------------------------

;to startup
;  setup
;end


to __setupExploration
  random-seed 0
set  refreshTime 200
;set  proba-innovation-sensitivity 0.08
 ;set Energy-accumulation-per-timestep-sensitivity 1

; param?tres de sc?narios
set  differenciation? Sdifferenciation 
set  technologie? Stechno
set  demographie? Snatalite
set  perturbations? Sperturbations

; param?tres initialisation
set  initialEnergy 15
set  Initial-population 10;100




; param?tres "de base"
set groups-weight 0.2;0.12;0.1


set  maximum-energy 20

set ressource-regrow-per-step 0.05

set groupesParVague 10
set nombreVagues 9;20
set tempsEntreDeuxVagues 800



set  displayCells? true
set  cornerconfiguration true
set  displayTechnicalOrColor true
set  robinet? false
set  angle-vision 0



  set techPlus 1
  set techMinus  0.1
  ; param?tres affichage
  ; a faire
  
 
  ;differenciation spatiale
  ifelse differenciation? 
  [
  set  proba-aller-en-mer 0.02;;;avant : 0.01 CHANGE0.13
set  porteeVoisinageLittoral 2; anciennement 4 (changement en juin 2013)
set  water-penalty 3
set  Energy-accumulation-per-timestep Energy-accumulation-per-timestep-sensitivity;1;0.02
set  initial-ressource-patches 1.2
  set  Carrying-capacity-consommee 1;0.1
  ; comme Young 0.01
   set  ProbaPartirSansRaison 0
   ; comme Young : 0.01;0;0.1
  ]
  [
  set  proba-aller-en-mer 0.25
set  porteeVoisinageLittoral 1
set  water-penalty 1
set  Energy-accumulation-per-timestep 0
set  initial-ressource-patches 0
set  Carrying-capacity-consommee 0;0.1
 set  ProbaPartirSansRaison 1;0.1
  ]
  
  ;techno
  ifelse technologie?
   [
  set  Innovation-probability proba-innovation-sensitivity;0.08;2.8E-4
set  Diffusion-probability 0.3;05;0.02548 
  set  feel-lonely-time 20
  set  deal-with-exchanges true
  ]
      [
  set  Innovation-probability 0
set  Diffusion-probability 0 
  set  feel-lonely-time 99999999
  set  deal-with-exchanges false
  ]
  ;demographie
  ifelse demographie?
  [
  set  natalite-probability proba-birth-sensitivity;1.8E-4
  ; comme Young : 0.0003
  
set  Energy-consumption-per-move groups-weight;0.05
set gestation-time 0;6 ; a articuler avec groups-weight
  ]
    [
  set  natalite-probability 0
set  Energy-consumption-per-move 0
set gestation-time 0
  ]
  ;perturbations
  ifelse perturbations?
  [
    set  probaPerturbation 0.0014
set perturb-tau 1500
  set  impact-perturbation 0.1
  ]
    [
    set  probaPerturbation 0
    set perturb-tau 999999999999

  set  impact-perturbation 0
  ]
  
;  initialiser toutes les valeurs dans le code
;  par groupe avec ifelse pour pouvoir "allumer" les trucs
;  puis par liste de sorte que tout peut ?tre fait dans le "behaviour space"
;  
end




to setup
  ;__clear-all-and-reset-ticks
  __setupExploration
  setup-globals
  setup-space
  setup-groups
end

to setup-space
  setup-uniform-world-25
  colorPatches;ask patches [set pcolor 65 - ressource-level];scale-color green ressource-level 2 0]
end

to colorPatches
 ifelse diplayMode [
 ask patches [
    ifelse displayCells? [
    ifelse (continent-name = 0)
    [set pcolor blue]
    [    set pcolor 65 - ressource-level]
    
    
    ][set pcolor 65]
    ]
  ][
  let temp max [countpresence] of patches

  ask patches [
      ifelse (temp = 0) [set pcolor black] [set pcolor 80 + 9.9 * countpresence / temp]]
 ]
end


to setup-uniform-world-4
  ask patches
  [
    ifelse (pxcor < (max-pxcor / 2))
    [  
      ifelse (pycor < (max-pycor / 2))
        [set continent-name "south-west"]
        [set continent-name "north-west"]
    ]
    [
      ifelse (pycor < (max-pycor / 2))
        [set continent-name "south-east"]
        [set continent-name "north-east"]
    ]
      set initial-ressource-level 1
    set ressource-level initial-ressource-level;random-float 1
  
  ]
end

to setup-uniform-world-25
  let pattern1 (list 2 3 4 2 3 2 1 2 1 0)
  let pattern2 (list 2 1 0 2 1 2 3 2 3 4)
  
  ;let pattern1 (list 2 0 1 2 3 2 1 0 0 0)
  ;let pattern2 (list 2 2 2 2 2 2 2 2 3 4)
  
  
  ;let pattern1 (list 4 3 3 3 3 3 3 2 2 2 2 2 2 2 2 2 1 1 1 1 1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0)
  ;let pattern2 pattern1
  ask patches
  [set initial-ressource-level -99
  let mx floor ((max-pxcor - min-pxcor + 1) / 4)
  let my   floor ((max-pycor - min-pycor + 1) / 4)
  let mx1  min-pxcor
  let mx2   min-pxcor + mx
  let mx3  min-pxcor + 2 * mx
  let mx4  min-pxcor + 3 * mx
  let mx5  min-pxcor + 4 * mx
  let my1  min-pycor
  let my2   min-pycor + my
  let my3  min-pycor + 2 * my
  let my4  min-pycor + 3 * my
  let my5  min-pycor + 4 * my

  
  if (pycor < my5 and pycor >= my4 and pxcor < mx2 and pxcor >= mx1) [
    set continent-name "continent-1"
    set initial-ressource-level initial-ressource-patches
    set pattern pattern1
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]
    if (pycor < my2 and pycor >= my1 and pxcor < mx4 and pxcor >= mx3) [
    set continent-name "continent-8"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern2
  ]
      if (pycor < my3 and pycor >= my2 and pxcor < mx4 and pxcor >= mx3) [
    set continent-name "continent-9"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern1
  ]
    if (pycor < my5 and pycor >= my4 and pxcor < mx3 and pxcor >= mx2) [
    set continent-name "continent-2"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern2
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]
    if (pycor < my2 and pycor >= my1 and pxcor < mx5 and pxcor >= mx4) [
    set continent-name "continent-7"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern1
  ]

  if (pycor < my5 and pycor >= my4 and pxcor < mx4 and pxcor >= mx3) [
    set continent-name "continent-3"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern1
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]
  
    if (pycor < my4 and pycor >= my3 and pxcor < mx3 and pxcor >= mx2) [
    set continent-name "continent-11"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern1
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]  
  
  if (pycor < my4 and pycor >= my3 and pxcor < mx4 and pxcor >= mx3) [
    set continent-name "continent-10"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern2
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]  
  
    if (pycor < my3 and pycor >= my2 and pxcor < mx5 and pxcor >= mx4) [
    set continent-name "continent-6"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern2
  ]
    
      if (pycor < my5 and pycor >= my4 and pxcor < mx5 and pxcor >= mx4) [
    set continent-name "continent-4"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern2
    ;NB : les noms ne correspondent ??? rien, c'est un h???ritage de l'autre fonction
  ]
    if (pycor < my4 and pycor >= my3 and pxcor < mx5 and pxcor >= mx4) [
    set continent-name "continent-5"
    set initial-ressource-level initial-ressource-patches
        set pattern pattern1
  ]
        ;if (pycor < my2 and pycor >= my1 and pxcor < mx2 and pxcor >= mx1) [
        if (pycor < my1 + 1 and pycor >= my1 and pxcor < mx1 + 1 and pxcor >= mx1) [
    set continent-name "island"
    set initial-ressource-level 99999999
    set pattern n-values length pattern1 [999999] 
    if initial-ressource-patches = 0 [ set initial-ressource-level 0]
  ]
    
    
  set ressource-level initial-ressource-level
  set littoral false
]

ask patches with [continent-name != 0]  [
if any? neighbors4 with [continent-name = 0] [
  ;if continent-name != "island"  [
  set littoral true;]
]

]

ask patches [
  set listNeighbors []
  let temp 0
  set temp patch (pxcor + 1) (pycor)
  if is-patch? temp [
    if [continent-name] of temp != 0 [
    set listNeighbors lput temp listNeighbors]
  ]
    set temp patch (pxcor - 1) (pycor)
  if is-patch? temp [
  if [continent-name] of temp != 0 [
  set listNeighbors lput temp listNeighbors]
  ]
    set temp patch (pxcor ) (pycor + 1)
  if is-patch? temp [
  if [continent-name] of temp != 0 [
  set listNeighbors lput temp listNeighbors]
  ]
    set temp patch (pxcor ) (pycor - 1)
  if is-patch? temp [
  if [continent-name] of temp != 0 [
  set listNeighbors lput temp listNeighbors]
  ]
  
  let i 2
  let tempL 0
  ;(list one-of neighbors4 one-of neighbors4 one-of neighbors4 one-of neighbors4)
  if littoral [
  
  
  while [i <= porteeVoisinageLittoral] [
  
  
   set temp patch (pxcor ) (pycor - i)
     if (is-patch? temp ) [ set tempL [littoral] of temp]
  if (is-patch? temp and tempL = true) [set listNeighbors lput temp listNeighbors]
  
  
  set tempL 0
     set temp patch (pxcor ) (pycor + i)
   if (is-patch? temp ) [ set tempL [littoral] of temp]
  if (is-patch? temp and tempL = true) [set listNeighbors lput temp listNeighbors]
    
  set tempL 0
   set temp patch (pxcor - i ) (pycor)
   if (is-patch? temp ) [ set tempL [littoral] of temp]
  if (is-patch? temp and tempL = true) [set listNeighbors lput temp listNeighbors]  
  set tempL 0
     set temp patch (pxcor + i ) (pycor)
   if (is-patch? temp ) [ set tempL [littoral] of temp]
  if (is-patch? temp and tempL = true) [set listNeighbors lput temp listNeighbors]
  set tempL 0
  set i i + 1
  ]
  ]
  
  
  
  
]
 
end

to setup-groups

  let still-to-create initial-population
  while [still-to-create > 0]
  [
    create-groups 1
    [
      set still-to-create (still-to-create - 1)
      ifelse cornerconfiguration [
      ;and pxcor / 2 = round (pxcor / 2) and pycor / 2 = round (pycor / 2)
        setup-group min-one-of patches with [count groups-here = 0 and continent-name != 0 ] [distance patch min-pxcor max-pycor] one-of listeCouleurs "god"
      ][
      setup-group one-of patches with [continent-name != 0 and continent-name != "island"] one-of listeCouleurs "god"
      ]
      
    ]
  ]
end

to setup-group [random-patch Oneculture padre]
 ; set has-moved? true
  set has-played? true
  set energy-level initialEnergy;random-float (2 * initialEnergy);random-normal 10 5
  set technical-level 1
  set nombreGroupesAyantVecu nombreGroupesAyantVecu + 1
 
  
  set heading random-float 360
  ;setxy max-pxcor max-pycor ; 
  setxy [pxcor] of random-patch [pycor] of random-patch
 ; set shape "person"
  set shape "square 3"
  set color 123
  set size technical-level
  set age 1
  set distance-done 0
  set continent [continent-name] of patch-here
  ; if trace-trajectories? [pen-down]
  
  set culture Oneculture
  set last-time-has-moved -1
  set last-time-has-exchanged -1
  set father padre
end

to setup-globals
set nombreVaguesEffectuees 0
set listeCouleurs [105 15]; 45]
  set total-distance 0
  set previous-total-distance 0
      set nIterations 1
  set listeContinents ( list "continent-1" "continent-2"  "continent-3"  "continent-4" "continent-5" "continent-6" "continent-7" "continent-8" "continent-9" "continent-10" "continent-11" "island")
  set listePopContinents (list 0 0 0 0 0 0 0 0 0 0 0 0)
  set listeRessourcesContinents (list 1 1 1 1 1 1 1 1 1 1 1 1)
end

;; ---------------------------------------------------------------------------------------
;; Main procedure
;; 1 - make groups move if necessary
;; 2 - perturbate
;; ---------------------------------------------------------------------------------------

to pre-treat
   ;set has-moved? false
   set has-played? false
   ;pd
end

to post-treat
  set has-played? true
  set age age + 1
    set continent [continent-name] of patch-here
    set maxtech max [technical-level] of groups
    ;ifelse displayTechnicalOrColor 
    ;[set color 128 - 5 * technical-level / maxtech]
    ;[set color culture]
    set size technical-level
    
end

to treat-groups
  ask groups[
    pre-treat
    ]
    ask groups with [has-played? = false] [
      treat
    ]
    ask groups[
    post-treat
  ]
end


to outputs
   my-update-plots
   ;color-patches-on-frontier
   ;tick
   if count groups > 0 [reportOutputs]
end

to reportOutputs
 
 if any? groups [
set AgeMoyenGroupes mean [age] of groups
set nombreGroupesALaFin count groups
set nombreCellulesOccupees count patches with [count groups-here > 0]
set nombreRegionsOccupees length remove 0 remove 1 remove-item 11 listePopContinents
ask groups [
  set distanceDepart distance patch min-pxcor max-pycor
  set distanceDepartCarre distanceDepartCarre + distanceDepart * distanceDepart
]
set distanceMoyenneDepart  mean [distanceDepart] of groups
set distanceMinDepart min [distanceDepart] of groups
set distanceMaxDepart max [distanceDepart] of groups
ifelse count groups > 1 [set distanceStandardDeviationDepart standard-deviation [distanceDepart] of groups][set distanceStandardDeviationDepart 9999]


  set nombreGroupesSurlIle count groups with [continent-name = "island"]
  set mintech min [technical-level] of groups
  set meantech mean [technical-level] of groups
  set mediantech median [technical-level] of groups
  set fractLittoral count groups with [[littoral] of patch-here] / count groups
  ifelse count groups > 1 [set standard-deviationtech standard-deviation [technical-level] of groups][set standard-deviationtech 9999]
set randomWalkDist sum [distanceDepartCarre] of groups / count groups
 ]
end


to go



if int (nIterations / tempsEntreDeuxVagues) =  (nIterations / tempsEntreDeuxVagues) and nombreVaguesEffectuees < nombreVagues [
  repeat groupesParVague [create-one-group]
  set nombreVaguesEffectuees nombreVaguesEffectuees + 1  
]
  treat-groups;if any? groups [treat-groups]
 ;perturbate-regional;if any? groups [perturbate-regional]
 perturbate-regional-fixed
 regrow-patches
 if any? groups [set nIterations nIterations + 1]
  if (nIterations / refreshTime) = floor (nIterations / refreshTime)[
    tick
     colorPatches
    outputs
    resetEvents
  ]
countpatches;;;
end

to countpatches
  ask groups [
    ask patch-here [set countpresence countpresence + 1]
;    let temp [countpresence] of patch-here 
;    set [countpresence] of patch-here temp + 1
  ]
end

to alternative-go
  resetEvents
  repeat refreshTime [
    
    if int (nIterations / tempsEntreDeuxVagues) =  (nIterations / tempsEntreDeuxVagues) and nombreVaguesEffectuees < nombreVagues [
  repeat groupesParVague [create-one-group]
  set nombreVaguesEffectuees nombreVaguesEffectuees + 1  
]
    treat-groups;if any? groups [treat-groups]
  ;perturbate-dynamic
 perturbate-regional-fixed
 regrow-patches;if any? groups [perturbate-regional]
 set nIterations nIterations + 1
 countpatches
 ifelse displayGroups [ask groups [set hidden? false]] [ask groups [set hidden? true]]
 
 ]

    tick
    colorPatches
    outputs
    
end


to-report has-to-leave?
  ;ifelse (((sum [ecological-weight technical-level] of groups-here) > ressource-level) or random-float 1 < ProbaPartirSansRaison)
  let temp random-float 1
 ; show "test"
  ifelse (((ecological-weight technical-level) > ressource-level) or temp < ProbaPartirSansRaison)
    [
            set typeMove 0
      ifelse count groups-here = 1  [set typeMove 2
        ;show "solo"
        ][set typeMove 3
       ; show "crowd"
       ]
      if temp < ProbaPartirSansRaison  [set typeMove 1]
      report true
      ]
    [  
      ;show "dontmove"
      report false    ]
end

to-report ecological-weight [tech]
  ;report max list (1 - 0.05  * tech) (0.1);1 / tech
  ; report max list (carrying-capacity-consommee * (1 - 0.05  * tech)) (0.1 * carrying-capacity-consommee)
  report groups-weight / tech
end

to-report innovate?
  report (random-float 1 < Innovation-probability)
end

;; ---------------------------------------------------------------------------------------
;; ---------------------------------------------------------------------------------------

;to diffusion-tech-level-locally
;  if any? other groups-here
;    [
;      let max-level max [technical-level] of groups-here
;      set technical-level max-level
;      ask groups-here [
;      if random-float 1 < Diffusion-probability [
;        set technical-level max-level
;      ]
;      ]
;    ]
;end

to diffusion-tech-level-locally-old
  if any? other groups-here and random-float 1 < Diffusion-probability
    [
      show "diffusion"
      show technical-level
      let max-level max [technical-level] of groups-here
      set technical-level max-level
      show technical-level
      set eventTechDiffusion eventTechDiffusion + 1
      ]
    
end

to diffusion-tech-level-locally
  if any? other groups-here [
  let tech technical-level
  let maxt max [technical-level] of other groups-here
  if tech > maxt and random-float 1 < Diffusion-probability
    [
      ask groups-here [
        set technical-level tech
      ]
      set eventTechDiffusion eventTechDiffusion + 1
      ]
]
end

to organize-steps
  
  ifelse (has-to-leave? = false)
  [
    set has-to-deal-with-accumulation true; 
    set has-to-deal-with-ecological-impacts true
    set eventDontMove eventDontMove + 1
    ;stop ; TO CHECK
  ]
  [
    ifelse (innovate? and [continent-name] of patch-here != 0)
    [set has-to-deal-with-innovation true]
    [set has-to-deal-with-migration true
      if typeMove = 1 [set eventMoveNoReason eventMoveNoReason + 1]
      if typeMove = 2 [set eventMoveAndAlone eventMoveAndAlone + 1]
      if typeMove = 3 [set eventMoveAndTwoMore eventMoveAndTwoMore + 1]
      ]
  ]
end

to deal-with-migration
  move-to-neighbor-cell
    ;  diffusion-tech-level-locally
      set last-time-has-moved 0  
end

;to change-cell
;     
;end


to deal-with-ecological-impacts
  let temp technical-level
  ask patch-here [
    let ressource-consumed carrying-capacity-consommee * ecological-weight temp
    ;if true [show ressource-consumed]
    set ressource-level ressource-level - ressource-consumed
    set ressource-to-recover ressource-to-recover + ressource-consumed
    ]
end

to deal-with-innovation
  set technical-level (technical-level + techPlus)
  set eventTechPlus eventTechPlus + 1
  if technical-level > maxTechAtteint [
  set maxTechAtteint technical-level
  ]
  diffusion-tech-level-locally
  ifelse (has-to-leave? = false) [
     set has-to-deal-with-accumulation true; 
     set has-to-deal-with-ecological-impacts true
  ][
     set has-to-deal-with-migration true
  ]
  

  
  
end

to deal-with-isolation-old
   if last-time-has-exchanged > feel-lonely-time [
    let temp technical-level
    ask groups-here with [technical-level >= 1 + techMinus] [
      ;show "isolation"
      ;show technical-level
     set technical-level temp - techMinus
        ;show technical-level
     set eventTechMinus eventTechMinus + 1
     set last-time-has-exchanged 0
    ]
    ; PB CA PEUT CONTINUER LONGTEMPS 
    ;set [technical-level] of groups-here technical-level - 1
  ]
end

to deal-with-isolation
   if last-time-has-exchanged > feel-lonely-time [
    let temp technical-level
  
      if (technical-level > 1) [
     set technical-level temp - techMinus
        ;show technical-level
     set eventTechMinus eventTechMinus + 1
     set last-time-has-exchanged 0
      ]
    
    ; PB CA PEUT CONTINUER LONGTEMPS 
    ;set [technical-level] of groups-here technical-level - 1
  ]
end


to deal-with-exchange
  if count other groups-here with [last-time-has-moved = 0] > 1 [
   set last-time-has-exchanged 0
   set culture [culture] of one-of other groups-here with [last-time-has-moved = 0]
  ]
end
to deal-with-survival
  if (energy-level <= 0)
    [
      ; ask patches with [pxcor >= (max-pxcor - (world-width / 4)) and pycor >= (max-pycor - (world-height / 4))]
      if robinet? [create-one-group]
      deathGroup
    ]
end

to-report limitEnergy [t]
  report maximum-energy * t
end

to deal-with-accumulation
  ;if (last-time-has-moved >= feel-sleepy-time) [set energy-level energy-level / 2]
 set energy-level (energy-level  + Energy-accumulation-per-timestep * groups-weight);Energy-accumulation-per-timestep)
 if energy-level > (limitEnergy technical-level) [ set energy-level limitEnergy technical-level]
 ;set size energy-level / 10
 

end

to deal-with-natality

 if natalite-probability > 0 [
  if random-float 1 < natalite-probability and last-time-has-moved >= gestation-time and continent != "island" [;* technical-level [
   set last-time-has-moved 0
   let tempG self
   ;if (ecological-weight technical-level * (count groups-here + 1) < ressource-level and last-time-has-moved > 100 and last-time-has-exchanged < 20 )  [
    let temp culture
   ask patch-here [
     sprout-groups 1
          [
            set eventBirth eventBirth + 1
            setup-group self temp tempG
          ]
          ]
   ]
  ] 
 
 end

to forget-everything
  set last-time-has-moved last-time-has-moved + 1
  set last-time-has-exchanged last-time-has-exchanged + 1
  set has-to-deal-with-migration false
  set has-to-deal-with-accumulation false
  set has-to-deal-with-innovation false
  set has-to-deal-with-exchange deal-with-exchanges 
  set has-to-deal-with-survival true
  set has-to-deal-with-ecological-impacts false
end

to treat
  forget-everything
  organize-steps
  if has-to-deal-with-innovation [deal-with-innovation]
  if has-to-deal-with-migration [deal-with-migration]
  if has-to-deal-with-accumulation [deal-with-accumulation]
  if has-to-deal-with-exchange [deal-with-exchange]
  if has-to-deal-with-exchange [deal-with-isolation]
  if has-to-deal-with-survival [deal-with-survival]
  if has-to-deal-with-ecological-impacts [deal-with-ecological-impacts]
  deal-with-natality
end

to create-one-group
let temp 0
ifelse cornerconfiguration [
      ;and pxcor / 2 = round (pxcor / 2) and pycor / 2 = round (pycor / 2)
        set temp min-one-of patches with [count groups-here = 0 and continent-name != 0 ] [distance patch min-pxcor max-pycor]
      ][
      set temp one-of patches with [continent-name != 0 and continent-name != "island"]
      ]


      ask  temp
        [
          sprout-groups 1
          [
            setup-group temp one-of listeCouleurs "waves"
          ]
        ]  
end

to move-to-neighbor-cell
      let temp1 true
      if [continent-name] of patch-here = "island" [
        set temp1 false
      ]
      ifelse angle-vision = 0 [
      
      ifelse [continent-name] of patch-here = 0 [
        ;ifelse any? neighbors4 with [continent-name != 0] [
        ;  move-to one-of neighbors4 with [continent-name != 0[
        ;[
          move-to one-of neighbors4
          if [continent-name] of patch-here != 0 and [continent-name] of patch-here != "island"  [
            set eventBackToContinent eventBackToContinent + 1 ]
        ;]
      ][
        ifelse [littoral] of patch-here = true [
          ifelse random-float 1 < proba-aller-en-mer [
            move-to one-of neighbors4 with [continent-name = 0]
            set eventGoToSea eventGoToSea + 1
          ][
            let potential-destination-cells [listNeighbors] of patch-here ;neighbors4; with [count groups-here = min [count groups-here] of neighbors4] ; tant que possible on fuit l'alterit???
              if length potential-destination-cells > 0 [move-to one-of potential-destination-cells]
          ]
        ][
              let potential-destination-cells [listNeighbors] of patch-here ;neighbors4; with [count groups-here = min [count groups-here] of neighbors4] ; tant que possible on fuit l'alterit???
              move-to one-of potential-destination-cells
        ]
      ]
      ]
      [
      fd 1
      set heading heading - angle-vision / 2 + random-float angle-vision
      let temp false
      if (round xcor = max-pxcor or round xcor = min-pxcor) [
        set temp true
      ]
       if (round ycor = max-pycor or round ycor = min-pycor) [
        set temp true
      ]
      if (temp) [
        set heading random 360;(- heading)
      ]
      ]
      
      if temp1 
      [
       ifelse [continent-name] of patch-here = "island" [
        set eventIslandArrival eventIslandArrival + 1
        set temp1 true
      ][set temp1 false]
      ]
      
      if temp1 [set nombreGroupesAyantTraverse nombreGroupesAyantTraverse + 1]
      set distance-done distance-done + 1
      set distanceTotaleParcourue distanceTotaleParcourue + 1
      ;set has-moved? true
      ifelse continent-name = 0 [
      set energy-level  (energy-level - water-penalty * Energy-consumption-per-move)  
      ][
      set energy-level  (energy-level - Energy-consumption-per-move)
      ]
end

to resetEvents
  set eventBirth 0
 set   eventDeath 0
 set   eventTechPlus 0
 set   eventTechDiffusion 0
  set  eventTechMinus 0
 set   eventMoveNoReason 0
 set   eventMoveAndAlone 0
 set   eventMoveAndTwoMore 0
 set   eventDontMove 0
 set   eventGoToSea 0
 set   eventBackToContinent 0
 set   eventIslandArrival 0
end

to deathGroup
  set eventDeath eventDeath + 1
  die
end

to-report color-indexed-on-technical-level
  report scale-color red technical-level min [technical-level] of groups max [technical-level] of groups
end


;to-report stop-run?
;  ifelse (count groups with [has-moved?]) / count groups < 0.1
;  [
;    report true
;  ]
;  [
;    report false
;  ]
;end
to perturbate-regional-fixed
  let i 0
    while [i < length listeContinents - 1] [ 
        ask patches with [continent-name = item i listeContinents] [
      ; set initial-ressource-level 0.000001 + 0.25 * item (floor(nIterations / perturb-tau) mod length pattern) pattern
      
       set initial-ressource-level 0.000001 + 0.5 * item (floor(nIterations / perturb-tau) mod length pattern) pattern;;;CHANGE
      set ressource-level initial-ressource-level - ressource-to-recover
    ]
  
    
           set i i + 1
   ]
end

to perturbate-regional
  if probaPerturbation > 0 [
  let i 0
  while [i < length listeContinents - 1] [ 
  ; A reprendre car la vraie proba que perturb positive ou negative = p( 1 - p )
    if random-float 1 < (probaPerturbation) [era-continent item i listeContinents impact-perturbation]
     if random-float 1 < (probaPerturbation) [era-continent item i listeContinents ( -1 * impact-perturbation)]
     set i i + 1
   ]
  
  ]
  

end

to regrow-patches
ask patches with [not any? groups-here] [
 ifelse ressource-to-recover >= ressource-regrow-per-step [
   set ressource-level ressource-level + ressource-regrow-per-step
   set ressource-to-recover ressource-to-recover - ressource-regrow-per-step
 
 ]
[
   set ressource-level ressource-level + ressource-to-recover
   set ressource-to-recover ressource-to-recover - ressource-to-recover
    
 ]
 ]
end

to era-continent [ _continent-name number]
    
  ask patches with [continent-name = _continent-name] [
      if initial-ressource-level + number <= 2 * initial-ressource-patches and initial-ressource-level + number >= 0 [
      set initial-ressource-level initial-ressource-level + number
      set ressource-level ressource-level + number
    ]
  ]
end

;to perturbate-dynamic
;if probaPerturbation > 0 [
;  ask patches [set ressource-level ressource-level - 0.1 + random-float 0.2
; ]
;repeat 0 [diffuse ressource-level 0.7]
;]
;ask patches [
; ifelse ressource-to-recover >= 0.1 [
;   set ressource-level ressource-level + 0.1
;   set ressource-to-recover ressource-to-recover - 0.1
; 
; ]
;[
;   set ressource-level ressource-level + ressource-to-recover
;   set ressource-to-recover ressource-to-recover - ressource-to-recover
;    
; ]
; ]
;  
;end



;; --------------------------------------------------------------------------------------- 
;; ---------------------------------------------------------------------------------------
to my-update-plots

let i 0
while [i < length listeContinents] [
  set listePopContinents replace-item i listePopContinents (count groups with [continent = item i listeContinents])
  set i i + 1
]
set i 0
while [i < length listeContinents] [
  set listeRessourcesContinents replace-item i listeRessourcesContinents ([initial-ressource-level] of one-of patches with [continent-name = item i listeContinents])
  set i i + 1
]



  
plots

end

to-report go-stop?
  ifelse nIterations > 1600
  [report true]
  [report false]
end

to run-to-grid
  setup
  while [not go-stop?] [go]
  reset-ticks  
end

to plots
  let temp0 0
  if count groups  > 0 [set temp0 mean [technical-level] of groups]
  set-current-plot "average-technical-level"
  plotxy ticks temp0
    set temp0 0
set-current-plot "TotalPopulation"
plotxy ticks count groups
      ;if count groups  > 0 [set temp0 sum [distanceDepartCarre] of groups / count groups]
      if count groups  > 0 [set temp0 mean [energy-level] of groups]
set-current-plot "average-energy-level"
plotxy ticks temp0
  end

to-report average-time-between-two-interactions
end

to-report average-distance-before-death
end

to-report average-time-of-inertia
end

to-report number-of-occupied-cells
end
@#$#@#$#@
GRAPHICS-WINDOW
497
68
928
520
-1
-1
10.53
1
11
1
1
1
0
0
0
1
0
39
0
39
0
0
1
pas de temps
30.0

BUTTON
19
48
204
81
NIL
while [ticks < 50] [go]
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

PLOT
9
83
491
320
average-technical-level
NIL
NIL
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" ""

PLOT
9
324
491
557
TotalPopulation
NIL
NIL
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" ""

PLOT
9
559
489
810
average-energy-level
NIL
NIL
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" ""

BUTTON
17
10
156
43
setup
;; (for this model to work with NetLogo's new plotting features,\n  ;; __clear-all-and-reset-ticks should be replaced with clear-all at\n  ;; the beginning of your setup procedure and reset-ticks at the end\n  ;; of the procedure.)\n  __clear-all-and-reset-ticks\n;; (for this model to work with NetLogo's new plotting features,\n  ;; __clear-all-and-reset-ticks should be replaced with clear-all at\n  ;; the beginning of your setup procedure and reset-ticks at the end\n  ;; of the procedure.)\n  ;__clear-all-and-reset-ticks \nsetup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SWITCH
338
30
485
63
Sperturbations
Sperturbations
0
1
-1000

SWITCH
680
27
784
60
Stechno
Stechno
0
1
-1000

SWITCH
512
29
662
62
Sdifferenciation
Sdifferenciation
0
1
-1000

SWITCH
800
28
905
61
Snatalite
Snatalite
0
1
-1000

BUTTON
207
10
326
43
NIL
alternative-go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
231
48
294
81
NIL
go
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
505
599
847
632
proba-innovation-sensitivity
proba-innovation-sensitivity
0
0.1
0.028
0.001
1
NIL
HORIZONTAL

SLIDER
505
563
847
596
Energy-accumulation-per-timestep-sensitivity
Energy-accumulation-per-timestep-sensitivity
0
2
1
0.1
1
NIL
HORIZONTAL

MONITOR
1074
415
1149
460
NIL
eventBirth
17
1
11

MONITOR
1073
465
1155
510
NIL
eventDeath
17
1
11

SLIDER
505
636
846
669
proba-birth-sensitivity
proba-birth-sensitivity
0
0.001
0.0010
0.00001
1
NIL
HORIZONTAL

MONITOR
904
538
1107
583
NIL
min [technical-level] of groups
17
1
11

SWITCH
932
89
1079
122
displayGroups
displayGroups
0
1
-1000

SWITCH
935
130
1100
163
diplayMode
diplayMode
0
1
-1000

BUTTON
936
175
1054
208
resetPresence
ask patches [set countpresence 0]
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
988
299
1139
344
distanceMoyenneDepart 
\nmean [distanceDepart] of groups
17
1
11

BUTTON
934
258
1083
291
initRandom
ask groups [die]\nwhile [count groups < 100] [\ncreate-groups 1 [\nset xcor min-pxcor + random (max-pxcor - min-pxcor)\nset ycor min-pycor + random (max-pycor - min-pycor)\n]\nask groups [\nif [continent-name] of patch-here = 0 [die]\nif [continent-name] of patch-here = \"island\" [die]\n]\n]\n\nask groups [\n  set distanceDepart distance patch min-pxcor max-pycor\n  set distanceDepartCarre distanceDepartCarre + distanceDepart * distanceDepart\n]
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
975
356
1061
401
NIL
count groups
17
1
11

@#$#@#$#@
## ## ## WHAT IS IT?

Ceci est une version hyper d??grad??e du mod??le de mobilit??s de groupes dans un espace diff??renci?? et soumis ?? des perturbations. On ne garde que 4 m??canismes :
     

- les perturbations venant affecter le niveau de ressources de l'environnement ;
    

- la mobilit?? ds groupes lorsqu'ils sont contraints (par choix al??atoire d'une cellule voisine(donc sans anticipation)) ;
    

- la d??pense et l'accumulation d'??nergie respectivement lorsqu'un groupe se d??place et lorsqu'il reste au m??me endroit ;
    

- la probabilit?? qu'un groupe a d'innover sous la contrainte.

A ces quatre m??canismes correpondent quatres param??tres :
    

- la fr??qence des perturbations, exprim??e comme un intervalle de temps (nombre de pas de temps) moyen entre deux perturbations cons??cutives ;
    

- le co??t ??nerg??tique associ?? ?? un d??placement ;
    

- le gain ??nerg??tique associ?? ?? un non-d??placement ;
    

- la probabilit?? our un groupe d'innover sous la contrainte.

L'espace est indiff??renci?? : toutes les cellules ont un niveau de ressources de 1. La reg??n??ration de la ressource est progressive et lin??aire sur l'ensemble de l'intervalle de temps n??cessaire ?? la r??cup??ration. Celui-ci est d'une dur??e tir??e au hasard uniform??ment entre 0 et l'intervalle de temps entre deux perturbations.

## ## ## HOW IT WORKS

This section could explain what rules the agents use to create the overall behavior of the model.

- l'innovation encourage la stabilit?? et l'accumulation de l'??nergie, et qui permet d'aller plus loin, sans que cela soit intentionnel.
    

- la mobilit?? encourage la diffusion de l'innovation
    

- une soci??t?? part avec son innovation
    

- diff??rencier les couleurs en fonction des niveau-techniques des individus

## ## ## HOW TO USE IT

This section could explain how to use the model, including a description of each of the items in the interface tab.

## ## ## THINGS TO NOTICE

This section could give some ideas of things for the user to notice while running the model.

## ## ## THINGS TO TRY

This section could give some ideas of things for the user to try to do (move sliders, switches, etc.) with the model.

## ## ## EXTENDING THE MODEL

This section could give some ideas of things to add or change in the procedures tab to make the model more complicated, detailed, accurate, etc.

## ## ## NETLOGO FEATURES

This section could point out any especially interesting or unusual features of NetLogo that the model makes use of, particularly in the Procedures tab.  It might also point out places where workarounds were needed because of missing features.

## ## ## RELATED MODELS

This section could give the names of models in the NetLogo Models Library or elsewhere which are of related interest.

## ## ## CREDITS AND REFERENCES

This section could contain a reference to the model's URL on the web if it has one, as well as any other necessary credits or references.
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

boat
true
0
Polygon -7500403 true true 0 180 60 270 240 270 300 180 0 180
Polygon -7500403 true true 105 15 60 150 285 150 105 15 120 15
Rectangle -7500403 true true 150 150 165 180

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

campsite
false
0
Polygon -7500403 true true 150 11 30 221 270 221
Polygon -16777216 true false 151 90 92 221 212 221
Line -7500403 true 150 30 150 225

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

sheep
false
0
Rectangle -7500403 true true 151 225 180 285
Rectangle -7500403 true true 47 225 75 285
Rectangle -7500403 true true 15 75 210 225
Circle -7500403 true true 135 75 150
Circle -16777216 true false 165 76 116

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

square 3
false
0
Rectangle -7500403 true true 240 30 270 270
Rectangle -7500403 true true 60 240 240 270
Rectangle -7500403 true true 30 30 60 270
Rectangle -7500403 true true 60 30 240 60

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.0.4
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
<experiments>
  <experiment name="TestV24Vintermediaire" repetitions="100" runMetricsEveryStep="true">
    <setup>setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="200"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
  </experiment>
  <experiment name="TestPerturbationsFalse" repetitions="100" runMetricsEveryStep="true">
    <setup>setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="200"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
  </experiment>
  <experiment name="TestV42" repetitions="30" runMetricsEveryStep="true">
    <setup>;; (for this model to work with NetLogo's new plotting features,
  ;; __clear-all-and-reset-ticks should be replaced with clear-all at
  ;; the beginning of your setup procedure and reset-ticks at the end
  ;; of the procedure.)
  __clear-all-and-reset-ticks
setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="100"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
    <steppedValueSet variable="proba-innovation-sensitivity" first="0.05" step="0.01" last="0.11"/>
    <steppedValueSet variable="Energy-accumulation-per-timestep-sensitivity" first="0.6" step="0.2" last="1.4"/>
  </experiment>
  <experiment name="variabilityV42" repetitions="100" runMetricsEveryStep="true">
    <setup>;; (for this model to work with NetLogo's new plotting features,
  ;; __clear-all-and-reset-ticks should be replaced with clear-all at
  ;; the beginning of your setup procedure and reset-ticks at the end
  ;; of the procedure.)
  __clear-all-and-reset-ticks
setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="300"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
    <steppedValueSet variable="proba-innovation-sensitivity" first="0.06" step="0.0050" last="0.07"/>
    <enumeratedValueSet variable="Energy-accumulation-per-timestep-sensitivity">
      <value value="1"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="variabilityV46-expB" repetitions="100" runMetricsEveryStep="true">
    <setup>;; (for this model to work with NetLogo's new plotting features,
  ;; __clear-all-and-reset-ticks should be replaced with clear-all at
  ;; the beginning of your setup procedure and reset-ticks at the end
  ;; of the procedure.)
  __clear-all-and-reset-ticks
setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="200"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
    <metric>eventBirth</metric>
    <metric>eventDeath</metric>
    <metric>eventTechPlus</metric>
    <metric>eventTechDiffusion</metric>
    <metric>eventTechMinus</metric>
    <metric>eventMoveNoReason</metric>
    <metric>eventMoveAndAlone</metric>
    <metric>eventMoveAndTwoMore</metric>
    <metric>eventDontMove</metric>
    <metric>eventGoToSea</metric>
    <metric>eventBackToContinent</metric>
    <metric>eventIslandArrival</metric>
    <enumeratedValueSet variable="proba-innovation-sensitivity">
      <value value="0.055"/>
    </enumeratedValueSet>
    <steppedValueSet variable="proba-birth-sensitivity" first="2.0E-5" step="2.0E-5" last="1.6E-4"/>
    <enumeratedValueSet variable="Energy-accumulation-per-timestep-sensitivity">
      <value value="1"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="variabilityV48" repetitions="60" runMetricsEveryStep="true">
    <setup>;; (for this model to work with NetLogo's new plotting features,
  ;; __clear-all-and-reset-ticks should be replaced with clear-all at
  ;; the beginning of your setup procedure and reset-ticks at the end
  ;; of the procedure.)
  __clear-all-and-reset-ticks
setup
outputs</setup>
    <go>alternative-go</go>
    <timeLimit steps="200"/>
    <metric>AgeMoyenGroupes</metric>
    <metric>randomWalkDist</metric>
    <metric>nombreGroupesAyantVecu</metric>
    <metric>distanceTotaleParcourue</metric>
    <metric>nombreGroupesAyantTraverse</metric>
    <metric>maxTechAtteint</metric>
    <metric>nombreGroupesALaFin</metric>
    <metric>nombreCellulesOccupees</metric>
    <metric>nombreRegionsOccupees</metric>
    <metric>distanceMoyenneDepart</metric>
    <metric>distanceMinDepart</metric>
    <metric>distanceMaxDepart</metric>
    <metric>distanceStandardDeviationDepart</metric>
    <metric>nombreGroupesSurlIle</metric>
    <metric>meantech</metric>
    <metric>mintech</metric>
    <metric>mediantech</metric>
    <metric>standard-deviationtech</metric>
    <metric>item 0 listePopContinents</metric>
    <metric>item 1 listePopContinents</metric>
    <metric>item 2 listePopContinents</metric>
    <metric>item 3 listePopContinents</metric>
    <metric>item 4 listePopContinents</metric>
    <metric>item 5 listePopContinents</metric>
    <metric>item 6 listePopContinents</metric>
    <metric>item 7 listePopContinents</metric>
    <metric>item 8 listePopContinents</metric>
    <metric>item 9 listePopContinents</metric>
    <metric>item 10 listePopContinents</metric>
    <metric>item 11 listePopContinents</metric>
    <metric>item 0 listeRessourcesContinents</metric>
    <metric>item 1 listeRessourcesContinents</metric>
    <metric>item 2 listeRessourcesContinents</metric>
    <metric>item 3 listeRessourcesContinents</metric>
    <metric>item 4 listeRessourcesContinents</metric>
    <metric>item 5 listeRessourcesContinents</metric>
    <metric>item 6 listeRessourcesContinents</metric>
    <metric>item 7 listeRessourcesContinents</metric>
    <metric>item 8 listeRessourcesContinents</metric>
    <metric>item 9 listeRessourcesContinents</metric>
    <metric>item 10 listeRessourcesContinents</metric>
    <metric>item 11 listeRessourcesContinents</metric>
    <metric>eventBirth</metric>
    <metric>eventDeath</metric>
    <metric>eventTechPlus</metric>
    <metric>eventTechDiffusion</metric>
    <metric>eventTechMinus</metric>
    <metric>eventMoveNoReason</metric>
    <metric>eventMoveAndAlone</metric>
    <metric>eventMoveAndTwoMore</metric>
    <metric>eventDontMove</metric>
    <metric>eventGoToSea</metric>
    <metric>eventBackToContinent</metric>
    <metric>eventIslandArrival</metric>
    <enumeratedValueSet variable="proba-innovation-sensitivity">
      <value value="0.028"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="proba-birth-sensitivity">
      <value value="1.3E-4"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Energy-accumulation-per-timestep-sensitivity">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Sperturbations">
      <value value="true"/>
      <value value="false"/>
    </enumeratedValueSet>
  </experiment>
</experiments>
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 1.0 0.0
0.0 1 1.0 0.0
0.2 0 1.0 0.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
