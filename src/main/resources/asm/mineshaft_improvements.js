function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    var getNewInstructions = function() {
        var newInstructions = new InsnList();
        var label = new LabelNode();
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 6));
        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 7));
        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "mineshaftGeneration", "(Lnet/minecraft/world/gen/feature/structure/StructurePiece;Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V", ASM.MethodType.STATIC));
        return newInstructions;
    }

    return {
        'mineshaft_improvements_corridor': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.structure.MineshaftPieces$Corridor',
                'methodName': 'func_230383_a_', // structure start maybe?
                'methodDesc': '(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z'
            },
            transformer: function(method) {
                var success = false;
                var arrayLength = method.instructions.size();
                var j = 0;

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IRETURN
                        && ++j == 2
                    ) {
                        method.instructions.insertBefore(instruction, getNewInstructions());
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (MineshaftImprovements) " + (success ? "Patched MineshaftPieces (corridor)" : "Failed to patch MineshaftPieces (corridor)"));
                return method;
            }
        },
        'mineshaft_improvements_room': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.structure.MineshaftPieces$Room',
                'methodName': 'func_230383_a_', // structure start maybe?
                'methodDesc': '(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z'
            },
            transformer: function(method) {
                var success = false;
                var arrayLength = method.instructions.size();
                var j = 0;

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IRETURN
                        && ++j == 2
                    ) {
                        var newInstructions = getNewInstructions();
                        method.instructions.insertBefore(instruction, newInstructions);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (MineshaftImprovements) " + (success ? "Patched MineshaftPieces (room)" : "Failed to patch MineshaftPieces (room)"));
                return method;
            }
        }
    }
}